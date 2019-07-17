public class Main {

    public static void main(String[] args) {
        // write your code here
        Integer[] nums = {-2,0,3,-5,2,-1};
        //应该这么创建
        /*SegmentTree<Integer> segTree = new SegmentTree<>(nums, new Merger<Integer>() {
            @Override
            public Integer merge(Integer a, Integer b) {
                return a+b;
            }
        });*/
        //也可以替换成lambda表达式
        SegmentTree<Integer> segTree = new SegmentTree<>(nums, (a,b) -> a+b);
        System.out.println(segTree);

        System.out.println(segTree.query(0, 2));
        System.out.println(segTree.query(2, 5));
        System.out.println(segTree.query(0, 5));


    }
}
