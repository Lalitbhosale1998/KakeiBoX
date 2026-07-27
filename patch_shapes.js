const fs = require('fs');
const file = 'app/src/main/java/com/lalit/kakeibox/ui/settings/SettingsScreen.kt';
let content = fs.readFileSync(file, 'utf8');

// 1. Add import
if (!content.includes('import com.personal.kakeibox.ui.components.toShape')) {
    content = content.replace('import androidx.compose.ui.text.font.FontWeight', 
        'import androidx.compose.ui.text.font.FontWeight\nimport com.personal.kakeibox.ui.components.toShape');
}

// 2. Update SettingsSelectorRow definition
const defTarget = `    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color? = null
) {`;
const defReplacement = `    onOptionSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
    accentColor: Color? = null,
    optionContent: (@Composable (T, String, Boolean) -> Unit)? = null
) {`;
content = content.replace(defTarget, defReplacement);

// 3. Update SettingsSelectorRow rendering
const renderTarget = `                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = label,
                                        style = MaterialTheme.typography.bodyLarge,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        color = optionTextColor
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Outlined.Check,
                                            contentDescription = "Selected",
                                            tint = if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }`;

const renderReplacement = `                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 14.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    if (optionContent != null) {
                                        optionContent(value, label, isSelected)
                                    } else {
                                        Text(
                                            text = label,
                                            style = MaterialTheme.typography.bodyLarge,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                            color = optionTextColor
                                        )
                                        if (isSelected) {
                                            Icon(
                                                imageVector = Icons.Outlined.Check,
                                                contentDescription = "Selected",
                                                tint = if (isSpaceTerminal) Color(0xFF46C2B4) else MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }
                                }`;
content = content.replace(renderTarget, renderReplacement);

// 4. Update the Card Shape calls

const shapeOptionContentCode = `
                        val shapeOptionContent: @Composable (CardShapePreference, String, Boolean) -> Unit = { value, label, isSelected ->
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(60.dp, 36.dp)
                                        .background(if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant, value.toShape(false)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    if (isSelected) {
                                        Icon(Icons.Outlined.Check, contentDescription = null, tint = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(16.dp))
                                    }
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Text(
                                    text = label,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }
`;

const shapeOptionsTarget1 = `                        val shapeOptions = listOf(
                            CardShapePreference.DEFAULT to "Default",
                            CardShapePreference.SEMICIRCLE to "Semicircle",
                            CardShapePreference.PILL to "Pill",
                            CardShapePreference.CLAMSHELL to "Clamshell",
                            CardShapePreference.SLANTED to "Slanted",
                            CardShapePreference.SQUARE to "Square"
                        )`;

content = content.replaceAll(shapeOptionsTarget1, shapeOptionsTarget1 + shapeOptionContentCode);

// Inject into Earnings
content = content.replaceAll(/title = "Earnings Card Shape",([\s\S]*?)accentColor = Color\(0xFF8B5CF6\)/g,
`title = "Earnings Card Shape",$1accentColor = Color(0xFF8B5CF6),
                                optionContent = shapeOptionContent`);

// Inject into Savings
content = content.replaceAll(/title = "Savings Card Shape",([\s\S]*?)accentColor = Color\(0xFF10B981\)/g,
`title = "Savings Card Shape",$1accentColor = Color(0xFF10B981),
                                optionContent = shapeOptionContent`);

// Inject into Remittance
content = content.replaceAll(/title = "Remittance Card Shape",([\s\S]*?)accentColor = Color\(0xFFEAB308\)/g,
`title = "Remittance Card Shape",$1accentColor = Color(0xFFEAB308),
                                optionContent = shapeOptionContent`);

fs.writeFileSync(file, content);
console.log("Patched shapes UI");
