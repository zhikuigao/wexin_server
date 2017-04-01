package com.jws.common.util;

/** 
 * @className:Levenshtein.java 
 * @classDescription:Levenshtein Distance 算法实现 ，全文相似度计算
 * 可以使用的地方：DNA分析 　　拼字检查 　　语音辨识 　　抄袭侦测 
 */  
public class Levenshtein {  
  
    public static void main(String[] args) {  
        //要比较的两个字符串  
        String str1 = "osition，翻译过来是奇异值分解，是一种矩阵分解的方法。其实，这个方法是提取一";  
        String str2 = "svd的全称是：Singular Value Decomposition，翻译过来是奇异值分解，是一种矩阵分解的方法。其实，这个方法是提取一般实矩阵“特征值”的算法，（这里特征值加引号是因为，特征值是针对方阵来定义的，而一般的m*n的实矩阵是没有特征值的。）其实，矩阵就是一个线性变换的表示方法，因为一个向量乘一个矩阵的结果是一个向量，第一个向量通过线性变换来变成第二个向量。线性变换有许多变换方向，比如你可以对一个图像矩阵做伸缩同时也做平移。那么特征值和特征向量又是什";  
        String str3 = "osition，翻译过来是奇异值分解，是一种矩阵分解的方法。其实，这个";
        float i1=levenshtein(str1,str2); 
        float i2=levenshtein(str1, str3);
        float i3=levenshtein(str2, str3);
    }
    
    
    public static float levenshtein(String str1,String str2) {  
        //计算两个字符串的长度。  
        int len1 = str1.length();  
        int len2 = str2.length();  
        //建立上面说的数组，比字符长度大一个空间  
        int[][] dif = new int[len1 + 1][len2 + 1];  
        //赋初值，步骤B。  
        for (int a = 0; a <= len1; a++) {  
            dif[a][0] = a;  
        }  
        for (int a = 0; a <= len2; a++) {  
            dif[0][a] = a;  
        }  
        //计算两个字符是否一样，计算左上的值  
        int temp;  
        for (int i = 1; i <= len1; i++) {  
            for (int j = 1; j <= len2; j++) {  
                if (str1.charAt(i - 1) == str2.charAt(j - 1)) {  
                    temp = 0;  
                } else {  
                    temp = 1;  
                }  
                //取三个值中最小的  
                dif[i][j] = min(dif[i - 1][j - 1] + temp, dif[i][j - 1] + 1,  
                        dif[i - 1][j] + 1);  
            }  
        }  
//        System.out.println("字符串\""+str1+"\"与\""+str2+"\"的比较");  
        //取数组右下角的值，同样不同位置代表不同字符串的比较  
//        System.out.println("差异步骤："+dif[len1][len2]);  
        //计算相似度  
        float similarity =1 - (float) dif[len1][len2] / Math.max(str1.length(), str2.length());  
//        System.out.println("相似度："+similarity);  
        return similarity;
    }  
  
    //得到最小值  
    private static int min(int... is) {  
        int min = Integer.MAX_VALUE;  
        for (int i : is) {  
            if (min > i) {  
                min = i;  
            }  
        }  
        return min;  
    }  
  
}