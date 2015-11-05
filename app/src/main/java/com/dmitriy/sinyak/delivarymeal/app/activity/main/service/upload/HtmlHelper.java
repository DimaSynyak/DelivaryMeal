package com.dmitriy.sinyak.delivarymeal.app.activity.main.service.upload;

import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 1 on 05.11.2015.
 */
public class HtmlHelper {
    TagNode rootNode;

    //�����������
    public HtmlHelper(URL htmlPage) throws IOException
    {
        //������ ������ HtmlCleaner
        HtmlCleaner cleaner = new HtmlCleaner();
        //��������� html ��� �����
        rootNode = cleaner.clean(htmlPage);
    }

    List<TagNode> getLinksByClass(String CSSClassname)
    {
        List<TagNode> linkList = new ArrayList<TagNode>();

        //�������� ��� ������
        TagNode linkElements[] = rootNode.getElementsByName("a", true);
        for (int i = 0; linkElements != null && i < linkElements.length; i++)
        {
            //�������� ������� �� �����
            String classType = linkElements[i].getAttributeByName("class");
            //���� ������� ���� � �� ������������ ��������, �� ��������� � ������
            if (classType != null && classType.equals(CSSClassname))
            {
                linkList.add(linkElements[i]);
            }
        }

        return linkList;
    }
}