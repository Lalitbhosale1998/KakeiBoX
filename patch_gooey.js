const fs = require('fs');

const path = 'app/src/main/java/com/lalit/kakeibox/ui/theme/KakeiboXApp.kt';
let content = fs.readFileSync(path, 'utf8');

const targetGooey = `                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .height(72.dp)
                                .shadow(8.dp, RoundedCornerShape(percent = 50))
                                .background(MaterialTheme.colorScheme.surfaceContainer, RoundedCornerShape(percent = 50))
                        ) {`;

const replacementGooey = `                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .windowInsetsPadding(WindowInsets.navigationBars)
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                                .height(72.dp)
                        ) {`;

if (content.includes(targetGooey)) {
    content = content.replace(targetGooey, replacementGooey);
    fs.writeFileSync(path, content);
    console.log("Patched gooey successfully!");
} else {
    console.log("Target gooey not found!");
}
