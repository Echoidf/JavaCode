package com.zql.components;

import java.io.Serializable;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.BitSet;
import java.util.Objects;

/**
 * 布隆过滤器
 *
 * @author：zql
 * @date: 2023/5/5
 */
public class BloomFilters<E> implements Serializable {
    private static final long serialVersionUID = -2326638072608273135L;

    /**
     * BitSet类： https://www.runoob.com/java/java-bitset-class.html
     * 一个Bitset类创建一种特殊类型的数组来保存位值。BitSet中数组大小会随需要增加
     */
    private BitSet bitset;

    private int bitSetSize;
    //每个元素要占的bit位数
    private double bitsPerElement;

    // 能过滤元素的最大值
    private int expectedNumberOfFilterElements;
    //实际上过滤器容器中的元素个数
    private int numberOfAddedElements;
    //哈希函数的个数
    private int k;
    //存储哈希值的字符编码
    static final Charset CHARSET = Charset.forName("UTF-8");

    /**
     * MessageDigest类支持MD5 SHA-1等算法：
     * https://www.yiibai.com/java_cryptography/java_cryptography_message_digest.html
     */
    static final String HASH_NAME = "MD5";
    static final MessageDigest DIGEST_FUNCTION;

    //初始化MessageDigest
    static {
        MessageDigest tmp;
        try {
            tmp = MessageDigest.getInstance(HASH_NAME);
        } catch (NoSuchAlgorithmException e) {
            tmp = null;
        }
        DIGEST_FUNCTION = tmp;
    }


    /**
     * @param c bitsPerElement
     * @param n 元素个数
     * @param k 哈希函数个数
     */
    public BloomFilters(double c, int n, int k) {
        this.expectedNumberOfFilterElements = n;
        this.k = k;
        this.bitsPerElement = c;
        /**
         * Math.ceil返回一个大于等于参数值的最小整值，如Math.ceil(2.2) == 3.0
         * bitSetSize >= 元素个数n * 每个元素要占的bit位数
         */
        this.bitSetSize = (int) Math.ceil(c * n);
        this.numberOfAddedElements = 0;
        this.bitset = new BitSet(bitSetSize);
    }

    public BloomFilters(int bitSetSize, int expectedNumberOfFilterElements) {
        this(bitSetSize / (double) expectedNumberOfFilterElements,
                expectedNumberOfFilterElements,
                (int) Math.round(bitSetSize / (double) expectedNumberOfFilterElements * Math.log(2.0)));
    }

    /**
     * @param falsePositiveProbability 假阳率
     * @param expectedNumberOfElements 元素个数
     */
    public BloomFilters(double falsePositiveProbability, int expectedNumberOfElements) {
        // c = k/ln(2)
        this(Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))) / Math.log(2),
                expectedNumberOfElements,
                // k = ln(2)m/n
                (int) Math.ceil(-(Math.log(falsePositiveProbability) / Math.log(2))));
    }

    /**
     * 按照传入的BitSet进行初始化
     *
     * @param bitSetSize
     * @param expectedNumberOfFilterElements
     * @param actualNumberOfFilterElements   预定义初始的过滤器中添加的元素个数
     * @param filterData                     预定义BitSet
     */
    public BloomFilters(int bitSetSize, int expectedNumberOfFilterElements,
                        int actualNumberOfFilterElements, BitSet filterData) {
        this(bitSetSize, expectedNumberOfFilterElements);
        this.bitset = filterData;
        this.numberOfAddedElements = actualNumberOfFilterElements;
    }


    /**
     * @param data 字节数组
     * @return 哈希值
     */
    public static long createHash(byte[] data) {
        long h = 0;
        byte[] res;

        synchronized (DIGEST_FUNCTION) {
            //使用 MessageDigest 类来进行哈希处理，默认UTF-8编码，MD5算法
            res = DIGEST_FUNCTION.digest(data);
        }

        /**
         * 通过4次循环将res数组的4个字节组成一个long类型的值赋给h返回
         * ((int) res[i]) & 0xFF 是res[i]的低8位
         * h |= ((int) res[i]) & 0xFF 按位或运算，将res[i]赋给h的低8位
         */
        for (int i = 0; i < 4; i++) {
            h <<= 8;
            h |= ((int) res[i]) & 0xFF;
        }
        return h;
    }

    public static long createHash(String val, Charset charset) {
        return createHash(val.getBytes(charset));
    }

    public static long createHash(String val) {
        return createHash(val, CHARSET);
    }

    //重写equals方法，主要是判断类型、属性值是否相等
    @Override
    public boolean equals(Object obj) {
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        final BloomFilters<E> other = (BloomFilters<E>) obj;
        if (this.expectedNumberOfFilterElements != other.expectedNumberOfFilterElements) {
            return false;
        }
        if (this.k != other.k) {
            return false;
        }
        if (this.bitSetSize != other.bitSetSize) {
            return false;
        }
        if (!Objects.equals(this.bitset, other.bitset)) {
            return false;
        }
        return true;
    }

    //重写hashcode方法
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 61 * hash + (this.bitset != null ? this.bitset.hashCode() : 0);
        hash = 61 * hash + this.expectedNumberOfFilterElements;
        hash = 61 * hash + this.bitSetSize;
        hash = 61 * hash + this.k;
        return hash;
    }
    /**
     * 获取假阳率
     *
     * @param numberOfElements 元素个数
     * @return
     */
    public double getFalsePositiveProbability(double numberOfElements) {
        // (1 - e^(-k * n / m)) ^ k
        return Math.pow((1 - Math.exp(-k * (double) numberOfElements
                / (double) bitSetSize)), k);
    }

    //按照实际元素个数获取假阳率
    public double getFalsePositiveProbability() {
        return getFalsePositiveProbability(numberOfAddedElements);
    }

    //按照元素最大值获取假阳率
    public double expectedFalsePositiveProbability() {
        return getFalsePositiveProbability(expectedNumberOfFilterElements);
    }

    public int getK() {
        return k;
    }

    public void clear() {
        bitset.clear();
        numberOfAddedElements = 0;
    }


}
