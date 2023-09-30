package synthesizer;

/**
 * @Description: hw1
 * @Author: whj
 * @Date: 2023-09-30 17:04
 */


public abstract class AbstractBoundedQueue<T> implements BoundedQueue<T> {
    protected int fillCount;
    protected int capacity;
    public int capacity(){
        return capacity;
    }

    public int fillCount(){
        return fillCount;
    }


}
