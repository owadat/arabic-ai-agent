package org.arabicagent.app;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.WindowManager;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.LinkedHashSet;
import java.util.Locale;

/** User-controlled, bounded WhatsApp channel session. No network access in this service. */
public final class ScreenAgentService extends AccessibilityService {
    static volatile ScreenAgentService instance;
    private static final String WHATSAPP="com.whatsapp";
    private final LinkedHashSet<String> lines=new LinkedHashSet<>();
    private boolean armed=false;
    private String target="";
    private int captures=0;
    private long deadline=0;
    private LinearLayout panel;
    private TextView status;
    private WindowManager wm;
    private final android.os.Handler timer=new android.os.Handler(android.os.Looper.getMainLooper());
    private final Runnable expire=()->stopSession();
    @Override protected void onServiceConnected(){super.onServiceConnected();instance=this;wm=(WindowManager)getSystemService(WINDOW_SERVICE);}
    @Override public void onAccessibilityEvent(AccessibilityEvent e){
        if(!armed)return;
        if(android.os.SystemClock.elapsedRealtime()>deadline){stopSession();return;}
        // Only check foreground identity; never collect event text.
        if(e.getEventType()==AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED||e.getEventType()==AccessibilityEvent.TYPE_WINDOWS_CHANGED){
            if(active()!=null)showPanel();else hidePanel();
        }
    }
    @Override public void onInterrupt(){stopSession();}
    @Override public void onDestroy(){stopSession();if(instance==this)instance=null;super.onDestroy();}
    void arm(String name){stopSession();target=name.trim();armed=true;deadline=android.os.SystemClock.elapsedRealtime()+300000;timer.postDelayed(expire,300000);if(active()!=null)showPanel();}
    void stopSession(){armed=false;timer.removeCallbacks(expire);hidePanel();lines.clear();captures=0;target="";}
    private AccessibilityNodeInfo active(){if(!armed)return null;AccessibilityNodeInfo r=getRootInActiveWindow();return r!=null&&WHATSAPP.contentEquals(r.getPackageName()==null?"":r.getPackageName())?r:null;}
    private int dp(int n){return (int)(n*getResources().getDisplayMetrics().density+.5f);}
    private Button button(String title,Runnable fn){Button b=new Button(this);b.setAllCaps(false);b.setText(title);b.setTextSize(12);b.setOnClickListener(v->fn.run());return b;}
    private void showPanel(){if(panel!=null||wm==null)return;LinearLayout p=new LinearLayout(this);p.setOrientation(LinearLayout.VERTICAL);p.setPadding(dp(6),dp(3),dp(6),dp(3));p.setBackgroundColor(0xf51d2635);p.setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
        status=new TextView(this);status.setTextColor(0xff76e4cb);status.setTextSize(12);status.setText("جلسة قراءة واتساب • لا يتم الإرسال تلقائياً");p.addView(status);
        LinearLayout row=new LinearLayout(this);row.setOrientation(LinearLayout.HORIZONTAL);
        for(Button b:new Button[]{button("التحديثات",()->navigate("التحديثات","Updates")),button("فتح القناة",()->navigate(target)),button("التقاط النص",()->capture()),button("تمرير",()->scroll())})row.addView(b,new LinearLayout.LayoutParams(0,-2,1));p.addView(row);
        LinearLayout row2=new LinearLayout(this);for(Button b:new Button[]{button("مراجعة النص",()->finish()),button("إيقاف ومسح",()->stopSession())})row2.addView(b,new LinearLayout.LayoutParams(0,-2,1));p.addView(row2);
        WindowManager.LayoutParams lp=new WindowManager.LayoutParams(-1,-2,WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY,WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL,PixelFormat.TRANSLUCENT);lp.gravity=Gravity.BOTTOM;
        try{wm.addView(p,lp);panel=p;}catch(Exception ignored){status=null;}
    }
    private void hidePanel(){if(panel!=null){try{wm.removeView(panel);}catch(Exception ignored){}panel=null;}status=null;}
    private void say(String s){if(status!=null)status.setText(s);}
    private static String normalized(CharSequence s){return s==null?"":s.toString().trim().replaceAll("\\s+"," ").toLowerCase(Locale.ROOT);}
    private AccessibilityNodeInfo find(AccessibilityNodeInfo n,String[] labels,int[] limit){if(n==null||limit[0]--<=0||n.isPassword()||n.isEditable())return null; if(n.isVisibleToUser())for(String label:labels){if(!label.isEmpty()&&(normalized(n.getText()).equals(normalized(label))||normalized(n.getContentDescription()).equals(normalized(label))))return n;}for(int i=0;i<n.getChildCount();i++){AccessibilityNodeInfo m=find(n.getChild(i),labels,limit);if(m!=null)return m;}return null;}
    private void navigate(String... labels){AccessibilityNodeInfo r=active();if(r==null){say("افتح واتساب أولاً");return;}AccessibilityNodeInfo n=find(r,labels,new int[]{1800});for(int i=0;n!=null&&i<5;i++,n=n.getParent()){if(n.isPassword()||n.isEditable())break;if(n.isClickable()&&n.performAction(AccessibilityNodeInfo.ACTION_CLICK)){say("تم الضغط على العنصر؛ تحقق من الشاشة");return;}}say("لم يظهر العنصر. افتحه يدوياً ثم تابع.");}
    private boolean containsTarget(AccessibilityNodeInfo r){
        if(target.isEmpty())return false;
        AccessibilityNodeInfo n=find(r,new String[]{target},new int[]{1800});
        if(n==null)return false;
        android.graphics.Rect b=new android.graphics.Rect(),window=new android.graphics.Rect();n.getBoundsInScreen(b);r.getBoundsInScreen(window);
        return !b.isEmpty()&&b.top<=window.top+window.height()/4;
    }
    private void collect(AccessibilityNodeInfo n,int[] limit){if(n==null||limit[0]--<=0||n.isPassword()||n.isEditable())return;if(n.isVisibleToUser()&&n.getText()!=null){String t=n.getText().toString().trim();if(!t.isEmpty()&&t.length()<=2500&&lines.size()<200)lines.add(t);}for(int i=0;i<n.getChildCount();i++)collect(n.getChild(i),limit);}
    private void capture(){AccessibilityNodeInfo r=active();if(r==null)return;if(captures>=10){say("بلغت حد 10 لقطات. راجع النص.");return;}if(!containsTarget(r)){say("اسم القناة غير ظاهر. افتح القناة المطلوبة أولاً.");return;}collect(r,new int[]{1800});captures++;say("تم التقاط النص الظاهر محلياً • "+captures+"/10");}
    private boolean scrollNode(AccessibilityNodeInfo n,int[] limit){if(n==null||limit[0]--<=0||n.isPassword()||n.isEditable())return false;if(n.isVisibleToUser()&&n.isScrollable()&&n.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD))return true;for(int i=0;i<n.getChildCount();i++)if(scrollNode(n.getChild(i),limit))return true;return false;}
    private void scroll(){AccessibilityNodeInfo r=active();if(r==null)return;if(!containsTarget(r)){say("افتح القناة المطلوبة أولاً");return;}say(scrollNode(r,new int[]{1800})?"تم التمرير؛ التقط النص بعد ظهوره":"تعذر التمرير الآلي؛ مرّر يدوياً");}
    private void finish(){StringBuilder b=new StringBuilder();for(String line:lines){if(b.length()+line.length()+1>12000)break;b.append(line).append('\n');}MainActivity.pendingScreenText=b.toString();stopSession();Intent i=new Intent(this,MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP);try{startActivity(i);}catch(Exception ignored){}}
}
