package wmlove.library;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by Administrator on 2016/9/17.
 */
public class SizeUtils {

    public  static int getSize(Context context){
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        return dm.widthPixels / 7;//屏幕宽度
    }
}
