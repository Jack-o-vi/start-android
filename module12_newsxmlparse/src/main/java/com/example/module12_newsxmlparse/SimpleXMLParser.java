package com.example.module12_newsxmlparse;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bjorn
 */
public class SimpleXMLParser {

    /**
     * News Item type definition. Every news item parsed from XML will be converted to NewsItem type object.
     *
     * @author Bjorn
     */
    public class NewsItem {
        public String title;
        public String link;
        public String description;

        public NewsItem(String title, String link, String description) {
            this.title = title;
            this.link = link;
            this.description = description;
        }
    }
    public List<NewsItem> parse(InputStream xml) {

        List<NewsItem> newsItems = null;

        XmlPullParser parser = Xml.newPullParser();

        try {
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);

            parser.setInput(xml, null);

            parser.nextTag();

            newsItems = readXML(parser);
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return newsItems;
    }

    private List<NewsItem> readXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        List<NewsItem> newsItems = new ArrayList<>();

        // At the beginning, expecting "rss" tag
        parser.require(XmlPullParser.START_TAG, null, "rss");
        while (parser.next() != XmlPullParser.START_TAG) {
            continue;
        }

        // Next level tag must be "channel"
        parser.require(XmlPullParser.START_TAG, null, "channel");

        // Loop till we encounter end tag "/channel"
        while (parser.next() != XmlPullParser.END_TAG) {

            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            // Check if the mext tag is an item
            String name = parser.getName();
            if(name.equals("item")){
                // readItem reads the news item and returns news item object. Add it to the list
                newsItems.add(readItem(parser));
            } else {

                // Skip tags we are not interested to parse
                skip(parser);
            }
        }

        return newsItems;
    }

    public NewsItem readItem(XmlPullParser parser) throws XmlPullParserException, IOException {

        parser.require(XmlPullParser.START_TAG, null, "item");

        String title = "";
        String description = "";
        String link = "";

        while(parser. next() != XmlPullParser.END_TAG){

            if(parser.getEventType() != XmlPullParser.START_TAG){
                continue;
            }

            String name = parser.getName();

            if(name.equals("title")){
                title = readElement(parser, "title");
            } else if(name.equals("link")){
                link = readElement(parser, "link");
            } else if(name.equals("description")){
                description = readElement(parser, "description");
            }else {
                skip(parser);
            }
        }

        return  new NewsItem(title, link, description);
    }

    private String readElement(XmlPullParser parser, String element) throws XmlPullParserException, IOException{

        String title;

        parser.require(XmlPullParser.START_TAG,null, element);

        title = readText(parser);

        parser.require(XmlPullParser.END_TAG, null, element);
        return title;
    }

    private String readText(XmlPullParser parser)  throws XmlPullParserException, IOException{
        String text = null;

        if(parser.next() == XmlPullParser.TEXT){
            text = parser.getText();
            parser.nextTag();
        }
        return text;
    }


    public void skip(XmlPullParser parser) throws XmlPullParserException, IOException {

        if(parser.getEventType() != XmlPullParser.START_TAG){
            throw new IllegalStateException();
        }

        int depth = 1;
        // Continue until er have skipped all elements recursivvely
        while(depth != 0){
            int next = parser.next();

            if(next == XmlPullParser.END_TAG){
                depth--;
            } else if (next == XmlPullParser.START_TAG){
                depth++;
            }

        }
    }

}
