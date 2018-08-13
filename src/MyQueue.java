import java.util.Stack;

/**
 * This class is the Queue class which is an implementation of a simple Queue-linked list
 * @author Alexander Park
 * @param <T> the object type parametrized
 * @version 1.0
 * @since 8-12-18
 */
public class MyQueue<T> {

    public static void main(String [] arg){
        MyQueue<Integer> nums = new MyQueue<Integer>();
        for(int i=0;i<10;i++){
            nums.enqueue(i);
        }
        System.out.println(nums.size());
        nums.dequeue();
        nums.enqueue(1000);
        while(nums.size()>0){

            System.out.println(nums.dequeue());
        }
        nums.enqueue(100);
        while(nums.size()>0){

            System.out.println(nums.dequeue());
        }
    }

    /**
     * Private internal Node class of the Queue
     * @param <T> object to be parametrized
     */
    private class Node<T>{
        private T data;
        private Node<T> next;

        public Node(T _data){
            data = _data;
        }

        public T getData(){
            return data;
        }

        public Node<T> getNext() {
            return next;
        }

        public void setNext(Node<T> next) {
            this.next = next;
        }

        public void setData(T data){
            this.data=data;
        }
    }

    private Node<T> sentinalHead=new Node<>(null);
    private Node<T> tail;
    private Stack<Node<T>> deadlinks = new Stack<Node<T>>(); //recycled nodes that are not garbage collected

    private int size;

    public MyQueue(){
        tail = sentinalHead;
    }

    /**
     * Enqueues data into the Queue
     * @param data the T object that will be added
     */
    public void enqueue(T data){
        Node<T> temp = null;
        if(deadlinks.size()==0){
            temp = new Node<T>(data);
        }else{
            temp = deadlinks.pop();
            temp.setData(data);
            temp.setNext(null);
        }

        tail.setNext(temp);
        tail=temp;
        size++;
    }

    /**
     * Removes the object in the front of the queue and returns it
     * @return the object that is in the front of the Queue
     */
    public T dequeue(){
       if(size==0){
           return null;
       }
        Node<T> temp = sentinalHead.getNext();
        sentinalHead.setNext(temp.getNext());
        T data = temp.getData();
        deadlinks.push(temp);
        size--;
        if(sentinalHead.getNext()==null){
            tail = sentinalHead;
        }

        return data;

    }

    /**
     * gets the size of the Queue
     * @return the size of queue
     */
    public int size(){
        return size;
    }


}
