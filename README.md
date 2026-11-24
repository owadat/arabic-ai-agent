# Arabic AI Agent — v0.2.0

## WhatsApp channel reading (experimental)

The app recognizes Arabic requests such as `فوت على واتساب في قناة عمر وسارة اقرا كل شي واعطيني ملخص`. Alternatively use the dedicated channel-session button. Enable the accessibility service yourself in Android settings, open the session and confirm the channel name. The panel offers explicit navigation, scroll and capture buttons. Open the desired channel manually if automatic label matching fails. Capture only content you are allowed to read; the panel collects exposed visible text (up to 10 captures, 200 unique lines, 12,000 characters for review) during a five-minute session. It does not read images, audio, protected views or full channel history. It may include interface text. Review and redact the editable capture, then explicitly approve sending it to the configured AI provider. The service has no network code and does not send messages, type, or collect passwords. Stop clears the temporary session. Disable accessibility when finished. This is user-guided, not unattended control. Android accessibility behavior and WhatsApp versions vary. Gemini connection settings remain as in v0.1.2; keys are session-only. If a new debug APK signature differs, export important notes before uninstalling the older build.

Build: upload all files in this update to the repository root (keep existing other files and GitHub Actions workflow). The existing prepare.sh workflow copies resources and builds the app. Android compilation and device testing have not been performed in the development environment. The pure Java parser tests and Java syntax checks have been run.

---

# Arabic AI Agent — v0.1.0

تطبيق أندرويد أصلي مفتوح المصدر (MIT)، بواجهة عربية، لتنظيم أوامر المستخدم وتنفيذ خطوات يوافق عليها بنفسه. Android 8+ (API 26).

## المتوفر في النسخة الأولى
- أوامر محلية محددة بالعربية، وخطة متعددة الخطوات (سطر لكل خطوة).
- فتح بحث Google في المتصفح، روابط HTTP(S)، التطبيقات المعروفة، الكاميرا والإعدادات.
- فتح لوحة الاتصال وتهيئة الرسائل دون إرسال تلقائي.
- إنشاء قوالب إعلان/منشور/سيناريو/برومبت محلياً، وحفظ الملاحظات محلياً.
- قراءة ملفات نصية UTF-8 حتى 100KB، نسخ ومشاركة وقراءة النص عبر أصوات النظام، وإملاء عبر خدمة النظام إذا كانت متوفرة.
- ربط اختياري بخادم Chat Completions متوافق مع OpenAI، عبر HTTPS أو HTTP على localhost فقط. أدخل عنوان endpoint الكامل واسم النموذج ومفتاحاً اختيارياً للجلسة. لا يوجد نموذج مدمج أو خدمة مجانية مضمونة. النص يرسل فقط بعد الموافقة، ولا تُنفذ مخرجات النموذج كأوامر.

## الحدود
لا يتحكم بالنقر داخل تطبيقات أخرى، ولا يرسل الرسائل أو يجري الاتصالات بنفسه، ولا يقرأ ملفات PDF، ولا يولد صوراً أو فيديوهات؛ البحث يفتح المتصفح ولا يجمع النتائج تلقائياً. القوالب المحلية ليست نموذجاً لغوياً. قد تستخدم خدمات الإملاء والنطق شبكة الإنترنت حسب الجهاز. التطبيق لا يطلب صلاحيات جهات الاتصال أو الرسائل أو الوصول الشامل أو Accessibility. الملاحظات محلية ويمكن حذفها؛ لا ترفع مفاتيح API إلى GitHub.

## البناء من GitHub (الهاتف أو الكمبيوتر)
1. ارفع ملفات هذا المجلد إلى جذر مستودع عام جديد.
2. أنشئ `.github/workflows/build-apk.yml` والصق محتوى ملف workflow المرفق (إذا لم ترفعه في المسار الصحيح).
3. من Actions افتح Build Android APK، ثم Run workflow إن لم يبدأ تلقائياً.
4. عند نجاح البناء، نزّل Arabic-AI-Agent-APK من Artifacts، فك الضغط وثبّت APK التجريبي. GitHub قد يطلب تسجيل الدخول. توقيع debug للتجربة فقط، وقد تحتاج إلى إزالة النسخة القديمة عند إعادة البناء بمفتاح مختلف (تفقد الملاحظات عند الإزالة). لا تعطّل حماية الجهاز العامة؛ اسمح بالتثبيت من المصدر الذي تثق به فقط.

## البناء المحلي
JDK 17، Gradle 8.9، Android SDK 35. من Bash: `bash prepare.sh` ثم `cd android-project && gradle assembleDebug`. افتح android-project في Android Studio إن أردت.

اختبار الأوامر: `mkdir -p /tmp/agent-tests && javac -encoding UTF-8 -d /tmp/agent-tests AgentCore.java AgentCoreTest.java && java -cp /tmp/agent-tests org.arabicagent.app.AgentCoreTest`.

## أمثلة
```
ابحث عن مطاعم المفرق
افتح واتساب
اكتب إعلان وجبة شاورما
احفظ ملاحظة فكرة فيديو
```

English: Native Android Arabic-first command assistant. Explicitly approved device intents, local templates and notes, optional user-configured AI text endpoint. No autonomous accessibility automation or built-in model. Experimental source release, not production-certified.
