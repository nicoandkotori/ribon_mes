package com.util;

import com.web.u8system.entity.U8Common;

import java.util.Calendar;

/**
 * Created by Vfun01 on 2017-12-01.
 */

public class ConnectString {


    public static String getPrefixByType(String str, int len, String strRule, String Vendor){
        if(str.equals("远程号"))
        {
            return "00";
        }

        else if (str.equals("日期") || str.equals("单据日期") )
        {
            String str11 = "";
            if (strRule.equals("年") )
            {
                Calendar cal = Calendar.getInstance();
                String year =String.valueOf(cal.get(Calendar.YEAR)) ;

                str11 =year;
            }
            else if (strRule.equals("年月") )
            {
                Calendar cal = Calendar.getInstance();
                String year =String.valueOf(cal.get(Calendar.YEAR)) ;
                String month="";
                if(cal.get(Calendar.MONTH )+1>9)
                {
                    month=String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                else
                {
                    month="0"+String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                str11 =year+month;
            }
            else if (strRule.equals("年月日") )
            {
                Calendar cal = Calendar.getInstance();
                String year =String.valueOf(cal.get(Calendar.YEAR)) ;
                String month="",day="";
                if(cal.get(Calendar.MONTH )+1>9)
                {
                    month=String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                else
                {
                    month="0"+String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                if(cal.get(Calendar.DAY_OF_MONTH )>9)
                {
                    day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH )) ;
                }
                else
                {
                    day="0"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH )) ;
                }
                str11 = year+month+day;
            }

            if (str11.length() < len)
            {
                while (str11.length() < len)
                {
                    str11 = "0" + str11;
                }
            }
            else
            {
                str11 = str11.substring(str11.length() - len, len);
            }
            return str11;
        }
        else
        {
            String str11 = "";
            while (str11.length() < len)
            {
                str11 = "0" + str11;
            }
           return str11;
        }
    }

    public static U8Common GetGlide(String CardNumber, String Glide, String GlideRule, String Vendor )
    {
        String dtStr;
        if (Glide.equals( "日期" )|| Glide.equals("单据日期"))
        {
            if (GlideRule.equals("年"))
            {
                Calendar cal = Calendar.getInstance();
                String year =String.valueOf(cal.get(Calendar.YEAR)) ;
                dtStr = year;
            }
            else if (GlideRule.equals("月") )
            {
                Calendar cal = Calendar.getInstance();
                String year =String.valueOf(cal.get(Calendar.YEAR)).substring(2,4) ;
                String month="";
                if(cal.get(Calendar.MONTH )+1>9)
                {
                    month=String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                else
                {
                    month="0"+String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                dtStr =year+month;
            }
            else
            {
                Calendar cal = Calendar.getInstance();
                String year =String.valueOf(cal.get(Calendar.YEAR)) ;
                String month="",day="";
                if(cal.get(Calendar.MONTH )+1>9)
                {
                    month=String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                else
                {
                    month="0"+String.valueOf(cal.get(Calendar.MONTH )+1) ;
                }
                if(cal.get(Calendar.DAY_OF_MONTH )>9)
                {
                    day=String.valueOf(cal.get(Calendar.DAY_OF_MONTH )) ;
                }
                else
                {
                    day="0"+String.valueOf(cal.get(Calendar.DAY_OF_MONTH )) ;
                }
                dtStr = year+month+day;
            }
            U8Common m=new U8Common();
            m.setGlide(Glide);
            m.setcCode(dtStr);
            m.setGlideRule(GlideRule);
            m.setCardNumber(CardNumber);
            return m;
        }


        else if(Glide.equals("供应商"))
        {
            U8Common m=new U8Common();
            m.setGlide(Glide);
            m.setcCode(Vendor);
            m.setGlideRule(GlideRule);
            m.setCardNumber(CardNumber);
            return m;

        }
        else
        {
            U8Common m=new U8Common();
            m.setGlide(null);
            m.setCardNumber(CardNumber);
            return m;
        }

    }

}
