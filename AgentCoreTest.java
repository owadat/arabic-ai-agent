package org.arabicagent.app;
public class AgentCoreTest {
 static int count=0;static void check(boolean v){count++;if(!v)throw new AssertionError("test "+count);}
 public static void main(String[] args){
 check(AgentCore.parse("ابحث عن مطاعم المفرق").get(0).arg.equals("مطاعم المفرق"));
 check(AgentCore.parse("إفتح واتساب").get(0).kind==AgentCore.Kind.APP);
 check(AgentCore.parse("افتح https://example.org").get(0).kind==AgentCore.Kind.OPEN);
 check(AgentCore.parse("اتصل ب 0791234567").get(0).arg.equals("0791234567"));
 check(AgentCore.parse("رسالة 0791234567 مرحبا").get(0).kind==AgentCore.Kind.SMS);
 check(AgentCore.parse("احفظ ملاحظة فكرة جديدة").get(0).arg.equals("فكرة جديدة"));
 check(AgentCore.parse("اكتب إعلان شاورما").get(0).kind==AgentCore.Kind.DRAFT);
 check(AgentCore.draft("اكتب إعلان شاورما").contains("شاورما"));
 check(AgentCore.parse("اقرأ ملف").get(0).kind==AgentCore.Kind.READ);
 check(AgentCore.parse("ابحث عن الأردن\nافتح واتساب؛مساعدة").size()==3);
 check(AgentCore.parse("   ").isEmpty());
 check(AgentCore.validNumber("+962 791234567"));check(!AgentCore.validNumber("*123#"));
 check(AgentCore.parse("اشرح لي الجاذبية").get(0).kind==AgentCore.Kind.AI);
 check(AgentCore.parse("اسأل الذكاء اشرح الجاذبية").get(0).arg.equals("اشرح الجاذبية"));
 System.out.println(count+" tests passed");}
}
