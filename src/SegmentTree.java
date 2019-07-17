/**
 * @author ZBK
 * @date 2019/7/15 - 23:45
 */

/**
 * @program: SegmentTree
 *
 * @description: 线段树SegmentTree
 *
 * @author: Zbk
 *
 * @create: 2019-07-15 23:45
 **/
public class SegmentTree<E> {

    //对于区间的每一个元素用户要获取,所以我们创建私有属性data
    private E[] data;
    //我们要将data组织成一个树的形式,也将使用数组的形式来保存
    private E[] tree;
    Merger<E> merger;

    //构造的参数是整个区间的范围,以数组的形式传进来
    public SegmentTree(E[] arr,Merger<E> merger){
        this.merger = merger;
        data = (E[]) new Object[arr.length];
        for (int i=0;i<arr.length;i++)
            data[i] = arr[i];

        //刚刚分析的,给树4倍的空间
        tree = (E[])new Object[4*data.length];

        //创建线段树
        
        buildSegmentTree(0,0,data.length-1);
    }

    /**
    * @Description: 在treeIndex位置创建表示区间[l...r]的线段树-->递归
    * @Param: [treeIndex, l, r] 参数treeIndex为创建的线段树根节点所对应的索引
     *                          l,r就是treeIndex这个节点所表示的区间左右端点
    * @return: void
    * @Author: ZBK
    * @Date: 2019/7/16
    */
    private void buildSegmentTree(int treeIndex,int l,int r) {
        if(l==r){
            //表示此使这个节点存储额信息就是他本身
            tree[treeIndex] = data[l];
            return;
        }

        //treeIndex节点的左右节点
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        //创建这个节点的左右子树
        //本来应该写成int mid = (l+r)/2,但是为了防止r+l过大内存溢出,这么写更合适
        int mid = l+(r-l)/2;
        //这样左子树范围就是[l,mid],右子树就是[mod+1,r]
        //创建左子树,右子树
        buildSegmentTree(leftTreeIndex, l, mid);
        buildSegmentTree(rightTreeIndex, mid+1, r);

        //不能这么写的,因为E没有定义能不能使用加法,而且我们也不希望能够只做加法,还希望用户能够根据业务自由组合业务逻辑
        // ,所以我们创建接口
        //tree[treeIndex] = tree[leftTreeIndex]+tree[rightTreeIndex];

        tree[treeIndex]=merger.merge(tree[leftTreeIndex],tree[rightTreeIndex]);
    }

    public int getsize(){
        return data.length;
    }

    public E get(int index){
        if (index<0||index>=data.length)
            throw new IllegalArgumentException("index is iellgea");
        return data[index];
    }

    //返回完全二叉树表示的数组中,一个索引的元素的左孩子的索引
    private int leftChild(int index){
        return 2*index+1;
    }


    //返回完全二叉树表示的数组中,一个索引的元素的右孩子的索引
    private int rightChild(int index){
        return 2*index+2;
    }


    // 返回区间[queryL, queryR]的值
    public E query(int queryL, int queryR){

        if(queryL < 0 || queryL >= data.length || queryR < 0 || queryR >= data.length || queryL > queryR)
            throw new IllegalArgumentException("Index is illegal.");

        return query(0, 0, data.length - 1, queryL, queryR);
    }

    /**
    * @Description:在以treeId为根的线段树中[l...r]的范围里,搜索区间[queyL...queryR]的值
    * @Param: [treeIndex, l, r] //递归函数要从某一个根节点开始,treeIndex就是根节点,lr就是根节点区间的边界值
     *          [queryL,queryR]就是要查询的区间
    * @return: E
    * @Author: ZBK
    * @Date: 2019/7/16
    */
    //可以将l,r包装起来,当然这里没有包装
    private E query(int treeIndex,int l,int r,int queryL,int queryR){
        if (l==queryL&&r==queryR)
            return tree[treeIndex];

        int mid = l+(r-l)/2;
        //要去左孩子查找,还是右孩子去查找,还是都要去找???下来就计算下,要去那里找
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        //如果用户要查询的左边界大于等于中间值,那么就去右孩子查
        if (queryL>=mid+1)
            return query(rightTreeIndex, mid+1, r, queryL, queryR);
        //如果用户要查的区间的有边界还小于mid
        else if (queryR<=mid)
            return query(leftTreeIndex, l, mid, queryL, queryR);
        //剩下一部分在左孩子,一部分在右孩子,那么两边都要查找
        //比如在左边查询,那么查询区间也要变了,变成mid
        E leftResult=query(leftTreeIndex, l, mid, queryL, mid);
        E rightResult = query(rightTreeIndex, mid+1, r, mid+1, queryR);
        //找完了,去找融合器融合
        return merger.merge(leftResult, rightResult);
    }

    //将index位置的值更新成用户传来的e
    public void set(int index,E e){
        //只是对一个值进行更新,整个过程跟二分搜索树中对一个值进行更新没有区别
        //先检查合法性
        if (index<=0||index>=data.length)
            throw new IllegalArgumentException("index is illegal");

        data[index] = e;
        set(0,0,data.length-1,index,e);
    }

    /** 
    * @Description: 在以treeIndex为根的线段树中更新index位置的值为e
    * @Param: [treeIndex, l, r, index, e] 
    * @return: void 
    * @Author: ZBK 
    * @Date: 2019/7/16 
    */ 
    private void set(int treeIndex, int l, int r, int index, E e) {

        //最小规模解
        if (l==r){
            tree[treeIndex] = e;
            return;
        }

        //
        int mid = l+(r-l)/2;
        int leftTreeIndex = leftChild(treeIndex);
        int rightTreeIndex = rightChild(treeIndex);

        if (index>=mid+1)
            set(rightTreeIndex, mid+1,r,index,e);
        else
            set(leftTreeIndex, l, mid, index, e);

        //我们还要注意这个节点的改变,那么他的相关父节点都要进行改变(也就是包含这个index的区间也要进行改变)
        //相应的treeIndex的左右孩子的值会发生改变,我们就不用管哪个孩子的值发生改变,直接使用merge
        tree[treeIndex] = merger.merge(tree[leftTreeIndex],   tree[rightTreeIndex]);
    }

    @Override
    public String toString() {
        StringBuilder res = new StringBuilder();
        res.append('[');
        for (int i=0;i<tree.length;i++){
            if (tree[i] != null)
                res.append(tree[i]);
            else
                res.append("null");
            if (i!=tree.length-1)
                res.append(", ");
        }

        res.append(']');
        return res.toString();
    }
}
