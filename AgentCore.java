package org.arabicagent.app;
import java.util.*;
import java.util.regex.*;

public final class AgentCore {
 public enum Kind { SEARCH, OPEN, APP, DIAL, SMS, SETTINGS, NOTE, DRAFT, AI, READ, HELP }
 public static final class Step { public final Kind kind; public final String arg; public Step(Kind k,String a){kind=k;arg=a;} }
 static String norm(String s){return s.trim().replaceAll("[\\u064B-\\u065F\\u0670]", "").replace('أ','ا').replace('إ','ا').replace('آ','ا').replace('ى','ي').toLowerCase(Locale.ROOT);}
 static String tail(String s,int n){String[] words=s.trim().split("\\s+",n+1);return words.length>n?words[n].trim():"";}
 public static List<Step> parse(String input){List<Step> out=new ArrayList<>();for(String raw:input.split("[\\n؛]+")){if(out.size()>=12)break;String s=raw.trim();if(s.isEmpty())continue;String n=norm(s);Kind k=Kind.AI;String a=s;
 if(n.equals("مساعدة")||n.equals("help")){k=Kind.HELP;a="";}
 else if(n.equals("اقرا ملف")||n.equals("قراءة ملف")){k=Kind.READ;a="";}
 else if(n.startsWith("ابحث عن ")){k=Kind.SEARCH;a=tail(s,2);}
 else if(n.startsWith("ابحث ")||n.startsWith("search ")){k=Kind.SEARCH;a=tail(s,1);}
 else if(n.startsWith("افتح ")||n.startsWith("open ")){a=tail(s,1);k=norm(a).startsWith("https://")||norm(a).startsWith("http://")?Kind.OPEN:Kind.APP;}
 else if(n.startsWith("اتصل ب ")){k=Kind.DIAL;a=tail(s,2);}
 else if(n.startsWith("اتصل ")){k=Kind.DIAL;a=tail(s,1);}
 else if(n.startsWith("رسالة ")||n.startsWith("sms ")){k=Kind.SMS;a=tail(s,1);}
 else if(n.equals("الاعدادات")||n.equals("اعدادات")||n.startsWith("اعدادات ")){k=Kind.SETTINGS;a=tail(s,1);}
 else if(n.startsWith("احفظ ملاحظة ")){k=Kind.NOTE;a=tail(s,2);}
 else if(n.startsWith("ملاحظة ")){k=Kind.NOTE;a=tail(s,1);}
 else if(n.startsWith("اكتب اعلان ")||n.startsWith("اكتب منشور ")||n.startsWith("اكتب سيناريو ")||n.startsWith("برومبت ")){k=Kind.DRAFT;a=s;}
 else if(n.startsWith("اسال الذكاء ")){k=Kind.AI;a=tail(s,2);}
 out.add(new Step(k,a));}return out;}
 public static boolean validNumber(String s){return s!=null&&s.matches("[+0-9 ()-]{3,30}")&&s.replaceAll("[^0-9]","").length()>=3;}
 public static String draft(String s){String n=norm(s);boolean prompt=n.startsWith("برومبت ");String topic=tail(s,prompt?1:2);if(topic.isEmpty())return "أضف اسم المنتج أو موضوع المحتوى.";
 if(prompt)return "الموضوع: "+topic+"\nوصف بصري واقعي، إضاءة طبيعية ناعمة، تكوين واضح، تفاصيل دقيقة، ألوان متوازنة، مساحة للنص، دون كتابة داخل الصورة.\nحدد نسبة العرض حسب المنصة.";
 if(n.startsWith("اكتب سيناريو "))return "سيناريو ريلز — "+topic+"\n0–3 ث: سؤال أو لقطة تشد الانتباه.\n3–15 ث: عرّف بالموضوع وميزته الحقيقية.\n15–25 ث: اعرض تجربة أو مثالاً مصوراً.\n25–30 ث: دعوة واضحة للتفاعل.\nأضف التفاصيل الموثقة قبل النشر.";
 return "بتدور على "+topic+"؟\nاكتشف التفاصيل واختر اللي بناسبك.\nتواصل معنا لمعرفة المزيد.\n\n[أضف ميزة حقيقية، العنوان، ورقم التواصل قبل النشر.]";}
 public static String label(Kind k){switch(k){case SEARCH:return "فتح نتائج البحث";case OPEN:return "فتح رابط";case APP:return "فتح تطبيق";case DIAL:return "فتح شاشة الاتصال";case SMS:return "تجهيز رسالة";case SETTINGS:return "فتح الإعدادات";case NOTE:return "حفظ ملاحظة محلياً";case DRAFT:return "إنشاء مسودة محلية";case AI:return "إرسال سؤال إلى محرك AI";case READ:return "اختيار ملف نصي";default:return "عرض المساعدة";}}
}
