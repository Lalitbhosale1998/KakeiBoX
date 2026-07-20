import re

file_path = "app/src/main/java/com/lalit/kakeibox/ui/theme/spend/SpendScreen.kt"

with open(file_path, "r", encoding="utf-8") as f:
    content = f.read()

# Pattern for the bouncy entrance springs
content = re.sub(
    r"spring\(stiffness\s*=\s*Spring\.StiffnessLow,\s*dampingRatio\s*=\s*Spring\.DampingRatioMediumBouncy\)",
    "ExpressivePhysics.fluidBouncy()",
    content
)

content = re.sub(
    r"spring\(Spring\.DampingRatioMediumBouncy,\s*Spring\.StiffnessLow\)",
    "ExpressivePhysics.fluidBouncy()",
    content
)

content = re.sub(
    r"spring\(dampingRatio\s*=\s*Spring\.DampingRatioMediumBouncy,\s*stiffness\s*=\s*Spring\.StiffnessLow\)",
    "ExpressivePhysics.fluidBouncy()",
    content
)

# Pattern for snappy physics
content = re.sub(
    r"spring\(dampingRatio\s*=\s*0\.6f,\s*stiffness\s*=\s*Spring\.StiffnessLow\)",
    "ExpressivePhysics.fluidSnappy()",
    content
)
content = re.sub(
    r"spring\(dampingRatio\s*=\s*0\.75f,\s*stiffness\s*=\s*Spring\.StiffnessLow\)",
    "ExpressivePhysics.fluidSnappy()",
    content
)
content = re.sub(
    r"spring\(dampingRatio\s*=\s*0\.55f,\s*stiffness\s*=\s*300f\)",
    "ExpressivePhysics.fluidSnappy()",
    content
)
content = re.sub(
    r"spring\(dampingRatio\s*=\s*0\.65f,\s*stiffness\s*=\s*Spring\.StiffnessLow\)",
    "ExpressivePhysics.fluidSnappy()",
    content
)
content = re.sub(
    r"spring\(stiffness\s*=\s*Spring\.StiffnessLow\)",
    "ExpressivePhysics.fluidSnappy()",
    content
)


with open(file_path, "w", encoding="utf-8") as f:
    f.write(content)

print("Done replacing physics in SpendScreen.kt")
