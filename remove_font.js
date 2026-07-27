const fs = require('fs');

// 1. Update SettingsScreen.kt to remove the App Font options
const settingsPath = 'app/src/main/java/com/lalit/kakeibox/ui/settings/SettingsScreen.kt';
let settingsContent = fs.readFileSync(settingsPath, 'utf8');

const regex1 = /SettingsSelectorRow\([\s\S]*?title = "App Font",[\s\S]*?accentColor = Color\(0xFFF59E0B\)\s*\)\s*HorizontalDivider\(modifier = Modifier\.padding\(horizontal = 16\.dp\), color = MaterialTheme\.colorScheme\.outlineVariant\.copy\(alpha = 0\.25f\)\)/g;
settingsContent = settingsContent.replace(regex1, '');

const regex2 = /if \(shouldShow\("App Font Face", keywords = listOf\("font", "typeface", "text", "style"\)\)\) \{[\s\S]*?SettingsSelectorRow\([\s\S]*?title = "App Font Family",[\s\S]*?accentColor = Color\(0xFFF59E0B\)\s*\)\s*\}/g;
settingsContent = settingsContent.replace(regex2, '');

fs.writeFileSync(settingsPath, settingsContent);
console.log("Removed from SettingsScreen.kt");

// 2. Update ThemeSettings.kt to use SYSTEM_SANS by default
const themeSettingsPath = 'app/src/main/java/com/lalit/kakeibox/data/preferences/ThemeSettings.kt';
let themeSettingsContent = fs.readFileSync(themeSettingsPath, 'utf8');
themeSettingsContent = themeSettingsContent.replace(/val appFont: AppFont = AppFont\.NUNITO/g, 'val appFont: AppFont = AppFont.SYSTEM_SANS');
fs.writeFileSync(themeSettingsPath, themeSettingsContent);
console.log("Updated ThemeSettings.kt");

// 3. Update UserPreferencesRepository.kt
const prefsPath = 'app/src/main/java/com/lalit/kakeibox/data/preferences/UserPreferencesRepository.kt';
let prefsContent = fs.readFileSync(prefsPath, 'utf8');
prefsContent = prefsContent.replace(/appFont = AppFont\.fromStorage\(prefs\[Keys\.APP_FONT\]\),/g, 'appFont = AppFont.SYSTEM_SANS,');
fs.writeFileSync(prefsPath, prefsContent);
console.log("Updated UserPreferencesRepository.kt");

// 4. Update Theme.kt to always use SYSTEM_SANS
const themePath = 'app/src/main/java/com/lalit/kakeibox/ui/theme/Theme.kt';
let themeContent = fs.readFileSync(themePath, 'utf8');
themeContent = themeContent.replace(/appFont: AppFont = AppFont\.NUNITO/g, 'appFont: AppFont = AppFont.SYSTEM_SANS');
fs.writeFileSync(themePath, themeContent);
console.log("Updated Theme.kt");
