import java.util.Stack;

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
    private Stack<Node<T>> deadlinks = new Stack<Node<T>>();

    private int size;

    public MyQueue(){
        tail = sentinalHead;
    }

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

    public int size(){
        return size;
    }


}
