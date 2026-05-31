$filePath = "app/src/main/java/com/lalit/kakeibox/ui/settings/SettingsScreen.kt"
$content = [System.IO.File]::ReadAllText($filePath)

# Normalize to LF
$content = $content.Replace("`r`n", "`n")

# 1. End of Visual Style section
$target1 = '                            shapeType = "clamshell",
                            onClick = { viewModel.setNavBarStyle(NavBarStyle.SPLIT) }
                        )
                    }
                }
                }
            }
        }'

$replacement1 = '                            shapeType = "clamshell",
                            onClick = { viewModel.setNavBarStyle(NavBarStyle.SPLIT) }
                        )
                    }
                }
                }
            }
        }
        }'

$content = $content.Replace($target1.Replace("`r`n", "`n"), $replacement1.Replace("`r`n", "`n"))

# 2. End of Personalization section
$target2 = '                                        Icon(
                                            imageVector = Icons.Outlined.DragHandle,
                                            contentDescription = "Drag to reorder",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    }
                }
            }'

$replacement2 = '                                        Icon(
                                            imageVector = Icons.Outlined.DragHandle,
                                            contentDescription = "Drag to reorder",
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f)
                                        )
                                    }
                                }
                            }
                        }
                    }
                    }
                }
            }
            }'

$content = $content.Replace($target2.Replace("`r`n", "`n"), $replacement2.Replace("`r`n", "`n"))

# Convert back to CRLF and save
$content = $content.Replace("`n", "`r`n")
[System.IO.File]::WriteAllText($filePath, $content, [System.Text.Encoding]::UTF8)
Write-Output "Settings Screen braces fixed successfully!"
