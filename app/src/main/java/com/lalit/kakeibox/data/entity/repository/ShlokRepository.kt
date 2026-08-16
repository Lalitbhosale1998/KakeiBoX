package com.personal.kakeibox.data.entity.repository

import com.personal.kakeibox.data.entity.ShlokEntry
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ShlokRepository @Inject constructor() {

    fun getSituationalShloks(): List<ShlokEntry> {
        return listOf(
            ShlokEntry(
                id = "shlok_inner_child",
                situationKey = "inner_child",
                situationTitleEn = "When the Inner Child Weeps for Love and Warmth",
                situationTitleMr = "जेव्हा मनातील लहान मुलाचा एकटेपणा जाणवतो (Tvameva Mata)",
                situationTitleJa = "インナーチャイルドと神聖な愛",
                whenToReadEn = "Read this when you feel the deep, old ache of loneliness from your childhood, and you need to remember that you are fiercely loved and held by the Divine.",
                whenToReadJa = "幼少期の寂しさを感じたとき、自分が神に強く愛されていることを思い出すための言葉。",
                sanskritText = "त्वमेव माता च पिता त्वमेव ।\nत्वमेव बन्धुश्च सखा त्वमेव ।\nत्वमेव विद्या द्रविणम् त्वमेव ।\nत्वमेव सर्वम् मम देव देव ॥",
                readingText = "Tvameva mata cha pita tvameva,\nTvameva bandhus cha sakha tvameva,\nTvameva vidya dravinam tvameva,\nTvameva sarvam mama deva deva.",
                englishMeaning = "O Supreme Lord, You are my Mother, and You are my Father. You are my family, and You are my truest friend. You are my knowledge, and You are my true wealth. O God of Gods, You are my everything.\n(Whenever you feel orphaned by the world, remember that the Supreme Parent has never left your side.)",
                marathiMeaning = "हे परमेश्वरा, तूच माझी माता आहेस आणि तूच माझा पिता आहेस. तूच माझा खरा नातेवाईक आणि तूच माझा सखा आहेस. तूच माझी विद्या आणि तूच माझी खरी संपत्ती आहेस. हे देवांच्या देवा, तूच माझे सर्वस्व आहेस.\n(जेव्हा जेव्हा तुला एकटेपणा जाणवेल, तेव्हा लक्षात ठेव की परमेश्वर स्वतः तुझ्या पाठीशी मायबाप बनून उभा आहे.)",
                recommendedTimeSlot = "night"
            ),
            ShlokEntry(
                id = "shlok_anxiety_gita",
                situationKey = "restlessness",
                situationTitleEn = "When Sudden Restlessness and Sadness Strike",
                situationTitleMr = "अचानक अस्वस्थता किंवा उदासी आल्यावर (Gita 2.14)",
                situationTitleJa = "突然の不安や悲しみに直面したとき",
                whenToReadEn = "Read this when your heart suddenly beats fast with anxiety, or a wave of sadness washes over you out of nowhere. This verse from the Bhagavad Gita (2.14) reminds you that feelings are just passing clouds.",
                whenToReadJa = "突然不安や悲しみに襲われたとき、感情は過渡的な雲であることを思い出す言葉。",
                sanskritText = "मात्रास्पर्शास्तु कौन्तेय शीतोष्णसुखदुःखदाः ।\nआगमापायिनोऽनित्यास्तांस्तितिक्षस्व भारत ॥",
                readingText = "Matra-sparshas tu kaunteya shitoshna-sukha-duhkha-dah,\nAgamapayino 'nityas tams titikshasva bharata.",
                englishMeaning = "O son of Kunti, the nonpermanent appearance of happiness and distress, and their disappearance in due course, are like the appearance and disappearance of winter and summer seasons. They arise from sense perception, O scion of Bharata, and one must learn to tolerate them without being disturbed.\n(This sudden sadness is just a passing winter breeze. It is not permanent. Do not let it shake your profound inner peace.)",
                marathiMeaning = "हे कुंतीपुत्रा (अर्जुना), इंद्रिय आणि विषय यांच्या संयोगामुळे निर्माण होणारे सुख आणि दुःख हे हिवाळा आणि उन्हाळ्यासारखे येणारे आणि जाणारे आहेत. ते कायम टिकणारे नाहीत. म्हणून, हे भरतवंशी अर्जुना, तू ते शांतपणे सहन करायला शिक.\n(ही अचानक आलेली उदासी फक्त एक ऋतू आहे, ती निघून जाईल. स्वतःला स्थिर ठेव.)",
                recommendedTimeSlot = "stress"
            ),
            ShlokEntry(
                id = "shlok_surrender_gita",
                situationKey = "surrender",
                situationTitleEn = "When You Fear the Divine Has Forgotten You",
                situationTitleMr = "परमेश्वराच्या आश्रयाची आणि भीतीमुक्तीची हमी (Gita 18.66)",
                situationTitleJa = "神の絶対的守護と帰依",
                whenToReadEn = "Read this when you feel terrified that you might be left alone, and you need the ultimate guarantee of Divine protection. This is Bhagwan Shri Krishna’s greatest promise of surrender (Bhagavad Gita 18.66).",
                whenToReadJa = "一人の恐怖を感じたとき、神の絶対的な保護を確信する言葉。",
                sanskritText = "सर्वधर्मान्परित्यज्य मामेकं शरणं व्रज ।\nअहं त्वा सर्वपापेभ्यो मोक्षयिष्यामि मा शुचः ॥",
                readingText = "Sarva-dharman parityajya mam ekam sharanam vraja,\nAham tva sarva-papebhyo mokshayishyami ma shuchah.",
                englishMeaning = "Abandon all varieties of dharmas (fears, attachments, and controlling the future) and simply surrender unto Me alone. I shall liberate you from all sinful reactions and sorrows. Do not fear.\n(The Lord is commanding you: \"Lalit, drop your heavy burdens. I am handling your timeline. Ma shuchah—Do not grieve, do not worry.\")",
                marathiMeaning = "सर्व धर्मांचा (आणि भविष्याच्या चिंतेचा) त्याग करून तू केवळ मला एकालाच शरण ये. मी तुला सर्व पापांतून आणि दुःखांतून मुक्त करीन. तू शोक करू नकोस (घाबरू नकोस).\n(तुझे सर्व ओझे भगवंताच्या चरणी अर्पण कर आणि निश्चिंत राहा. भगवंत तुझी काळजी घेत आहेत.)",
                recommendedTimeSlot = "courage"
            ),
            ShlokEntry(
                id = "shlok_active_waiting",
                situationKey = "duty",
                situationTitleEn = "When the Wait Feels Too Long (Active Waiting)",
                situationTitleMr = "कर्तव्य पार पाडताना आणि आजवर लक्ष केंद्रित करताना (Gita 2.47)",
                situationTitleJa = "今なすべき職務への集中",
                whenToReadEn = "Read this when you are doing your daily duties—going to work, practicing the guitar, or stepping into the dojo—and you need to focus on the present moment without obsessing over the destination (Bhagavad Gita 2.47).",
                whenToReadJa = "日々の義務・ギターの練習・道場での稽古の際、今日に集中するための言葉。",
                sanskritText = "कर्मण्येवाधिकारस्ते मा फलेषु कदाचन ।\nमा कर्मफलहेतुर्भूर्मा ते सङ्गोऽस्त्वकर्मणि ॥",
                readingText = "Karmany-evadhikaras te ma phaleshu kadachana,\nMa karma-phala-hetur bhur ma te sango 'stv akarmani.",
                englishMeaning = "You have a right to perform your prescribed duty, but you are not entitled to the fruits of action. Never consider yourself the cause of the results of your activities, and never be attached to not doing your duty.\n(Focus entirely on being the best version of yourself today. The wife, the family, the home—these are the fruits. Let the Divine deliver the fruits when the season is right.)",
                marathiMeaning = "तुझा अधिकार केवळ कर्म करण्यात (तुझे कर्तव्य पार पाडण्यात) आहे, त्याच्या फळांवर तुझा कोणताही अधिकार नाही. तू स्वतःला कर्माच्या फळाचे कारण मानू नकोस आणि कर्म न करण्याकडे (आळसाकडे) तुझी प्रवृत्ती नसू दे.\n(आजचा दिवस सार्थकी लाव, फळाची (भविष्याची) चिंता भगवंतावर सोडून दे.)",
                recommendedTimeSlot = "office_duty"
            ),
            ShlokEntry(
                id = "shlok_pure_intention",
                situationKey = "pure_intention",
                situationTitleEn = "The Promise of Your Pure Intention",
                situationTitleMr = "शुद्ध इच्छेच्या पूर्ततेची हमी (Mundaka Upanishad 3.1.10)",
                situationTitleJa = "純粋な意図と望みの成就",
                whenToReadEn = "Read this when you doubt if your dream of a loving Japanese wife and a peaceful family will actually come true. This Upanishadic verse guarantees that a heart purified of selfishness always receives what it desires.",
                whenToReadJa = "純粋で愛に満ちた家庭の夢に対する疑いが生じたとき、望みが叶うことを確信する言葉。",
                sanskritText = "यं यं लोकं मनसा संविभाति\nविशुद्धसत्त्वः कामयते यांश्च कामान् ।\nतं तं लोकं जयते तांश्च कामां-\nस्तस्मादात्मज्ञं ह्यर्चयेद् भूतिकामः ॥",
                readingText = "Yam yam lokam manasa samvibhati,\nVishuddha-sattvah kamayate yams cha kaman;\nTam tam lokam jayate tams cha kaman,\nTasmad atmajnam hy archayed bhuti-kamah.",
                englishMeaning = "Whatever world a man of pure heart (vishuddha-sattvah) desires, and whatever pure wishes he entertains in his mind, he absolutely attains those worlds and those desires. Therefore, one should honor the Divine within.\n(Your desire is not born of lust; it is born of a pure wish to heal and love unconditionally. The universe is bound by its own laws to manifest it.)",
                marathiMeaning = "ज्या गोष्टींची किंवा इच्छांची शुद्ध अंतःकरणाचा मनुष्य (ज्याचे मन मोहातून मुक्त झाले आहे) मनापासून कामना करतो, त्या सर्व इच्छा आणि जग त्याला निश्चितपणे प्राप्त होते.\n(तुझी घर आणि कुटुंबाची इच्छा अत्यंत शुद्ध आहे, ती पूर्ण होणे हा निसर्गाचा नियम आहे. भगवंतावर विश्वास ठेव.)",
                recommendedTimeSlot = "office_start"
            ),
            ShlokEntry(
                id = "shlok_mental_discipline",
                situationKey = "mental_discipline",
                situationTitleEn = "The Discipline of the Peaceful Wait",
                situationTitleMr = "मनःशांती, संयम आणि मानसिक तप (Gita 17.16)",
                situationTitleJa = "精神的平穏と自主的修練",
                whenToReadEn = "Read this before you practice your guitar, before you enter the dojo, or when you are simply walking the streets of Koto City. It reminds you that the greatest preparation for a husband is the mastery and gentleness of his own mind.",
                whenToReadJa = "ギターの練習前、道場に入る前、江東区の街を walk するときの心の修練の言葉。",
                sanskritText = "मनःप्रसादः सौम्यत्वं मौनमात्मविनिग्रहः ।\nभावसंशुद्धिरित्येतत्तपो मानसमुच्यते ॥",
                readingText = "Manah-prasadah saumyatvam maunam atma-vinigrahah,\nBhava-samshuddhir ityetat tapo manasam uchyate.",
                englishMeaning = "Serenity of thought, gentleness, silence, self-control, and absolute purity of purpose—all these together are declared as the austerity (Tapasya) of the mind.\n(A sovereign man does not wait with anxiety. He waits with a gentle, controlled mind. The stillness of your sword practice and the melody of your guitar are the exact practices of this mental serenity.)",
                marathiMeaning = "मनाची प्रसन्नता, सौम्यपणा, मौन (शांतता), आत्मसंयम आणि भावांची (हेतूची) पूर्ण शुद्धता, या सर्वांना 'मानसिक तप' असे म्हणतात.\n(प्रतीक्षा करताना मन अस्वस्थ होऊ न देणे आणि स्वतःला शांत, संयमी व आनंदी ठेवणे हेच तुझे सध्याचे सर्वात मोठे तप आहे.)",
                recommendedTimeSlot = "evening"
            ),
            ShlokEntry(
                id = "shlok_matchmaker",
                situationKey = "matchmaker",
                situationTitleEn = "Trusting the Ultimate Matchmaker",
                situationTitleMr = "परमेश्वराच्या अचूक मार्गदर्शनावर विश्वास (Gita 18.61)",
                situationTitleJa = "神の導きと運命の出会いへの信頼",
                whenToReadEn = "Read this when you feel the urge to desperately search for her, or when you wonder how you will ever find her in a massive city like Tokyo. This verse reminds you that you do not need to orchestrate the meeting; the Divine is already guiding her steps.",
                whenToReadJa = "東京の大都市で探す焦りを感じたとき、神が互いの歩みを導いていることを思い出す言葉。",
                sanskritText = "ईश्वरः सर्वभूतानां हृद्देशेऽर्जुन तिष्ठति ।\nभ्रामयन्सर्वभूतानि यन्त्रारूढानि मायया ॥",
                readingText = "Ishvarah sarva-bhutanam hrid-dese 'rjuna tishthati,\nBhramayan sarva-bhutani yantrarudhani mayaya.",
                englishMeaning = "O Arjuna, the Supreme Lord resides in the hearts of all living beings, directing their wanderings, who are seated as on a machine made of the material energy.\n(The very same Supreme Lord who dwells in your heart also dwells in hers. He knows exactly where you both are. Let the Lord direct your paths until they naturally intersect.)",
                marathiMeaning = "हे अर्जुना, परमेश्वर सर्व प्राणिमात्रांच्या हृदयात वास करतो आणि आपल्या मायेने यंत्रावर आरूढ असलेल्यांसारखे सर्व प्राण्यांना त्यांच्या कर्मानुसार फिरवतो आणि अचूक मार्गदर्शन करतो.\n(तुझ्या भावी पत्नीच्या हृदयातही तोच परमेश्वर आहे. तोच योग्य वेळी (Kala) आणि योग्य ठिकाणी (Desha) तुम्हा दोघांची भेट घडवून आणेल. तुला शोधण्याची गरज नाही, फक्त तयार राहा.)",
                recommendedTimeSlot = "morning"
            )
        )
    }
}
