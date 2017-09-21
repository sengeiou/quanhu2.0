package com.rz.circled.widget.pinyin;

import android.text.TextUtils;
import android.util.Log;

import com.rz.common.utils.StringUtils;
import com.rz.httpapi.bean.FriendInformationBean;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * Created by xiayumo on 16/8/19.
 */
public class SearchPingyinEngine {

    /**
     * 按群号-群名拼音搜索
     *
     * @param str
     */
    public static ArrayList<FriendInformationBean> searchGroup(String str,
<<<<<<< HEAD
                                                               List<FriendInformationBean> allContacts) {
=======
                                                    List<FriendInformationBean> allContacts) {
>>>>>>> 2540931ec03580503cb88e4fe7ef18497de3b69c

        Log.e("tag", "搜索关键字＝" + str);

        ArrayList<FriendInformationBean> groupList = new ArrayList<>();
        CharacterParser finder = CharacterParser.getInstance();
        /**
         *优先匹配电话号码
         */
        if (finder.hasNumeric(str)) {
            for (FriendInformationBean group : allContacts) {
                if (!StringUtils.isEmpty(group.getCustPhone())) {
                    if ((group.getCustPhone() + "").equals(str)) {
                        groupList.add(group);
                    }
                }
            }

            /**
             * 如果匹配成功不会进行下一步，否则进去下一步，因为昵称里面可能会有数字
             */
            if (groupList.size() > 0) {
                return groupList;
            } else {
                /**
                 * 数字匹配昵称
                 */
                for (FriendInformationBean group : allContacts) {
                    if (!StringUtils.isEmpty(group.getCustNname()) && (group.getCustNname() + "").contains(str)) {
                        groupList.add(group);
                    } else if (!StringUtils.isEmpty(group.getNameNotes()) && (group.getNameNotes() + "").contains(str)) {
                        groupList.add(group);
                    }
                }
                return groupList;
            }
        }

        /**
         * 其次匹配汉字
         */
        if (finder.hasCH(str.toString())) {
            for (FriendInformationBean group : allContacts) {
                if (!StringUtils.isEmpty(group.getCustNname()) && (group.getCustNname() + "").contains(str)) {
                    groupList.add(group);
                } else if (!StringUtils.isEmpty(group.getNameNotes()) && (group.getNameNotes() + "").contains(str)) {
                    groupList.add(group);
                }
            }
            /**
             * 汉字不管有没有都返回
             */
            return groupList;
        }

        /**
         * 如果不是号码匹配和汉字匹配，进入字母汉字模糊匹配
         */

        String result = "";
        for (FriendInformationBean group : allContacts) {
            // 先将输入的字符串转换为拼音
            finder.setResource(str);
            result = finder.getSpelling();
            if (contains(group, result)) {
                groupList.add(group);
            }
        }
        return groupList;
    }

    /**
     * 根据拼音搜索
     *
     * @return
     */
    private static boolean contains(FriendInformationBean group, String search) {
        if (TextUtils.isEmpty(group.getCustNname())) {
            return false;
        }

        boolean flag = false;

//        Log.e("Tag","匹配字符串＝"+search);

        // 简拼匹配,如果输入在字符串长度大于5就不按首字母匹配了
//        if (search.length() < 5) {
        String firstLetters;
        firstLetters = CharacterParser.getInstance().getFirstSelling(group.getCustNname());
        if (!TextUtils.isEmpty(group.getNameNotes())) {
            firstLetters = firstLetters + CharacterParser.getInstance().getFirstSelling(group.getNameNotes());
        }
        Log.e("Tag", "首字母简拼＝" + firstLetters);
        // 不区分大小写
        Pattern firstLetterMatcher = Pattern.compile(search,
                Pattern.CASE_INSENSITIVE);
        flag = firstLetterMatcher.matcher(firstLetters).find();

        /**
         * 如果搜索首字母无法匹配任何一个首字母，就不用进行全拼匹配（优化搜索显示机制）
         */

        if (!firstLetters.contains(search.substring(0, 1))) {
            return false;
        }

//        }

        /**
         * 全拼屏蔽掉，没有意义
         */
//        if (!flag) { // 如果简拼已经找到了，就不使用全拼了
//            // 全拼匹配
//            CharacterParser finder = CharacterParser.getInstance();
//            finder.setResource(group.getCustNname());
//            // 不区分大小写
////            Log.e("Tag","字符串全拼＝"+finder.getSpelling());
//            Pattern pattern2 = Pattern
//                    .compile(search, Pattern.CASE_INSENSITIVE);
//            Matcher matcher2 = pattern2.matcher(finder.getSpelling());
//            flag = matcher2.find();
//        }

        return flag;
    }

    /**
     * 优先检索电话号码，其次是汉字
     * 最后匹配字母，如果字母里存在
     * 简拼优先匹配简拼最后匹配全拼
     * 剩下的情况一律不匹配
     *
     * @param info
     * @param searchStr
     * @return
     */
    public static int[] getkeyIndex(FriendInformationBean info, String searchStr) {

        int[] indexs = {0, 1, 0};

        ArrayList<Integer> keyIndexs = new ArrayList<>();

        CharacterParser finder = CharacterParser.getInstance();

        if (finder.hasNumeric(searchStr)) {
            //优先匹配电话号码
            indexs[0] = (info.getCustNname()).indexOf(searchStr);
            indexs[1] = indexs[0] + searchStr.length();
            indexs[2] = 2;
        } else if (finder.hasCH(searchStr)) {
            //其次匹配汉字
            indexs[0] = (info.getCustNname()).indexOf(searchStr);
            indexs[1] = indexs[0] + searchStr.length();
            indexs[2] = 0;
            if (indexs[0] == -1 && !TextUtils.isEmpty((info.getNameNotes()))) {
                indexs[0] = (info.getNameNotes()).indexOf(searchStr);
                indexs[1] = indexs[0] + searchStr.length();
                indexs[2] = 1;
            }
        } else if (finder.hasLetter(searchStr)) {
            String firstLetters, firstLetters1 = "";
            firstLetters = finder.getFirstSelling((info.getCustNname()));
            if (!TextUtils.isEmpty((info.getNameNotes())))
                firstLetters1 = finder.getFirstSelling((info.getNameNotes()));
            if (firstLetters.contains(searchStr.toUpperCase())) {
                indexs[0] = firstLetters.indexOf(searchStr);
                indexs[1] = indexs[0] + searchStr.length();
                indexs[2] = 0;
            } else if (firstLetters1.contains(searchStr.toUpperCase())) {
                indexs[0] = firstLetters1.indexOf(searchStr);
                indexs[1] = indexs[0] + searchStr.length();
                indexs[2] = 1;
            }
            /**
             * 全拼屏蔽掉，没有意义
             */
//             else{
//                 String[]lettersArray=allLetters.split(" ");
//                 for(int i=0;i<lettersArray.length;i++){
//                     if(searchStr.contains(lettersArray[i].toLowerCase())){
//                         keyIndexs.add(i);
//                         searchStr.replace("",lettersArray[i]);
//                     }
//                 }
//                 indexs[0]=keyIndexs.get(0);
//                 indexs[1]=keyIndexs.get(keyIndexs.size()-1);
//                 if(!StringUtils.isEmpty(searchStr)){
//                     indexs[1]=indexs[1]+1;
//                 }
//             }
        } else {
            //其他情况一律不匹配

        }

        return indexs;

    }

    /**
     * 根据全拼搜索字符串获取返回的开始和终点位置
     */
    public static int[] getIndexOfStr(String chs, String parentChs) {
        int[] indexs = {0, 1};
        int indexStart = parentChs.indexOf(chs);
        indexs[0] = indexStart;
        indexs[1] = indexStart + chs.length() - 1;
        return indexs;
    }


}
