package system._scatter.scripts;

import com.quest.wcf.core.util.ColorUtils;
import java.awt.Color;

colors = functionHelper.invokeFunction("system:test20tree20map.colorPatternMapping", selectColorPattern );
    
if (metricValue==null) metricValue = 0;

def h = new BigDecimal(metricValue / maxMetricValue).setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue() * 100;


int colorIndex =h / colors.size() as int;
if (colorIndex  == colors.size()) {
    colorIndex  -= 1;
}
def colorHgb = colors.get(colorIndex);

def color =  Color.decode(colorHgb);

return color;