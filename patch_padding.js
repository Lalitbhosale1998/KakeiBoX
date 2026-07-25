const fs = require('fs');

const path = 'app/src/main/java/com/lalit/kakeibox/ui/theme/KakeiboXApp.kt';
let content = fs.readFileSync(path, 'utf8');

const target = `    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            SharedTransitionLayout {
                NavHost(`;

const replacement = `    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                // ONLY apply top padding, so content scrolls UNDER the transparent floating bottom bar!
                .padding(top = innerPadding.calculateTopPadding(), bottom = 0.dp)
        ) {
            SharedTransitionLayout {
                NavHost(`;

if (content.includes(target)) {
    content = content.replace(target, replacement);
    fs.writeFileSync(path, content);
    console.log("Patched padding successfully!");
} else {
    console.log("Target not found!");
}
