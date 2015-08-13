package cn.com.hewoyi.deletetool.Utils;

import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * 创建者: small wai
 * 日期: 2015-07-30
 * 时间: 11:20
 * 内容：解析本地xml文件获取白名单
 * PS；更新下一个版本后换成网络获取白名单
 * 修改者：
 * 修改时间及内容：
 */
public class UtilForXml {

    private List<String> whiteList = new ArrayList<>();

    /**
     * 解析本地xml文件获取白名单
     *
     * @return 返回白名单list
     */
    public List<String> queryFromXml() {
        try {
            InputStream inputStream = MyApplication.getContext().getAssets().open("white.xml"); // 从assets文件夹里读取xml文件
            XmlPullParserFactory factory;
            factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            // setInput()方法将返回的 XML 数据设置进去就可以开始解析
            xmlPullParser.setInput(inputStream, "UTF-8");
            // getEventType()可以得到当前的解析事件
            int eventType = xmlPullParser.getEventType();

            String name; // 创建各实例以及对应的ID
            String packageName;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String nodeName = xmlPullParser.getName();
                switch (eventType) {
                    // 开始解析app结点
                    case XmlPullParser.START_TAG: {
                        if ("app".equals(nodeName)) {
                            // 获取app节点的name属性和package属性
                            name = xmlPullParser.getAttributeValue(null,
                                    "name");
                            packageName = xmlPullParser.getAttributeValue(null,
                                    "package");
                            //解析完后加入whiteList里
                            whiteList.add(packageName);
                            //Log.d("xml", name + " : " + packageName);
                        }
                        break;
                    }// 完成解析List结点
                    case XmlPullParser.END_TAG: {
                        if ("List".equals(nodeName)) {
                            //完成解析之后？待写
                        }
                        break;
                    }
                    default:
                        break;
                }
                eventType = xmlPullParser.next();
            }
            inputStream.close(); // 关闭流

        } catch (Exception e) {
            //未完善
            Toast.makeText(MyApplication.getContext(), "加载失败",
                    Toast.LENGTH_SHORT).show();
        }
       /* for (String list:whiteList){
            Log.d("list",list);
        }*/
        return whiteList;
    }
}
