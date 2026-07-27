const fs = require('fs');
const path = 'app/src/main/java/com/lalit/kakeibox/ui/settings/SettingsScreen.kt';

let content = fs.readFileSync(path, 'utf8');

const target1 = `                                selectedValueLabel = themeSettings.appFont.name.lowercase().replaceFirstChar { it.uppercase() },
                                options = listOf(
                                    AppFont.NUNITO to "Nunito",
                                    AppFont.MONOSPACE to "Monospace",
                                    AppFont.SYSTEM_SANS to "System Sans",
                                    AppFont.OUTFIT to "Outfit",
                                    AppFont.PLAYFAIR to "Playfair"
                                ),`;

const replacement1 = `                                selectedValueLabel = if (themeSettings.appFont == AppFont.SYSTEM_SANS) "System" else themeSettings.appFont.name.lowercase().replaceFirstChar { it.uppercase() },
                                options = listOf(
                                    AppFont.NUNITO to "Nunito",
                                    AppFont.MONOSPACE to "Monospace",
                                    AppFont.SYSTEM_SANS to "System",
                                    AppFont.OUTFIT to "Outfit",
                                    AppFont.PLAYFAIR to "Playfair"
                                ),`;

content = content.replace(target1, replacement1);
content = content.replace(target1, replacement1); // Replace the second occurrence too if they are identical

fs.writeFileSync(path, content);
console.log("Patched font names successfully!");
