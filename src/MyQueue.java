import java.util.Stack;

public class MyQueue<T> {

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
        }

        tail.setNext(temp);
        tail=temp;
        size++;
    }

    public T dequeue(){
        if(size<=0){
            tail = sentinalHead;
            return null;
        }
        Node<T> temp = sentinalHead.getNext();
        sentinalHead.setNext(temp.getNext());
        T data = temp.getData();
        deadlinks.push(temp);
        size--;

        return data;

    }

    public int size(){
        return size;
    }
}
