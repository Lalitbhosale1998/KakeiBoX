$content = Get-Content app/src/main/java/com/lalit/kakeibox/ui/theme/spend/SpendScreen.kt -Raw -Encoding UTF8

$content = $content.Replace("spring(stiffness = Spring.StiffnessLow, dampingRatio = Spring.DampingRatioMediumBouncy)", "ExpressivePhysics.fluidBouncy()")
$content = $content.Replace("spring(Spring.DampingRatioMediumBouncy, Spring.StiffnessLow)", "ExpressivePhysics.fluidBouncy()")
$content = $content.Replace("spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow)", "ExpressivePhysics.fluidBouncy()")

$content = $content.Replace("spring(dampingRatio = 0.6f, stiffness = Spring.StiffnessLow)", "ExpressivePhysics.fluidSnappy()")
$content = $content.Replace("spring(dampingRatio = 0.75f, stiffness = Spring.StiffnessLow)", "ExpressivePhysics.fluidSnappy()")
$content = $content.Replace("spring(dampingRatio = 0.55f, stiffness = 300f)", "ExpressivePhysics.fluidSnappy()")
$content = $content.Replace("spring(dampingRatio = 0.65f, stiffness = Spring.StiffnessLow)", "ExpressivePhysics.fluidSnappy()")
$content = $content.Replace("spring(stiffness = Spring.StiffnessLow)", "ExpressivePhysics.fluidSnappy()")

Set-Content -Path app/src/main/java/com/lalit/kakeibox/ui/theme/spend/SpendScreen.kt -Value $content -NoNewline -Encoding UTF8
