$files = @(
    "app/src/main/java/com/lalit/kakeibox/ui/settings/SettingsScreen.kt",
    "app/src/main/java/com/lalit/kakeibox/ui/KakeiboXLockScreen.kt",
    "app/src/main/java/com/lalit/kakeibox/ui/components/RoundedParallaxCarousel.kt",
    "app/src/main/java/com/lalit/kakeibox/ui/components/WheelPicker.kt",
    "app/src/main/java/com/lalit/kakeibox/ui/theme/spend/SpendScreen.kt"
)

foreach ($file in $files) {
    if (Test-Path $file) {
        $content = Get-Content $file -Raw -Encoding UTF8
        
        # Replace 32dp and 28dp with extraLarge
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*32\.dp\s*\)', 'MaterialTheme.shapes.extraLarge')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*28\.dp\s*\)', 'MaterialTheme.shapes.extraLarge')
        
        # Replace 24dp and 20dp with large
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*24\.dp\s*\)', 'MaterialTheme.shapes.large')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*20\.dp\s*\)', 'MaterialTheme.shapes.large')
        
        # Replace 16dp and 12dp with medium
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*16\.dp\s*\)', 'MaterialTheme.shapes.medium')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*12\.dp\s*\)', 'MaterialTheme.shapes.medium')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*10\.dp\s*\)', 'MaterialTheme.shapes.medium')
        
        # Replace 8dp and 6dp with small
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*8\.dp\s*\)', 'MaterialTheme.shapes.small')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*6\.dp\s*\)', 'MaterialTheme.shapes.small')
        
        # Replace 4dp and 3dp with extraSmall
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*4\.dp\s*\)', 'MaterialTheme.shapes.extraSmall')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(\s*3\.dp\s*\)', 'MaterialTheme.shapes.extraSmall')

        # Complex ones:
        $content = [regex]::Replace($content, 'RoundedCornerShape\(topStart = 12\.dp, topEnd = 12\.dp\)', 'MaterialTheme.shapes.medium.copy(bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp), bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp))')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(topStart = 28\.dp, topEnd = 28\.dp\)', 'MaterialTheme.shapes.extraLarge.copy(bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp), bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp))')
        $content = [regex]::Replace($content, 'RoundedCornerShape\(topStart = 32\.dp, topEnd = 32\.dp\)', 'MaterialTheme.shapes.extraLarge.copy(bottomStart = androidx.compose.foundation.shape.CornerSize(0.dp), bottomEnd = androidx.compose.foundation.shape.CornerSize(0.dp))')

        Set-Content -Path $file -Value $content -NoNewline -Encoding UTF8
    }
}
