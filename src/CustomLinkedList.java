/**
 * Custom made LinkedList for simple paths
 * @author Alexander Park
 * @version 1.0
 * @since 8-12-18
 */
public class CustomLinkedList{
    /**
     * Private internal node class
     */
    private class Node{
        Node next;
        RoomEntry data;

        public void setNext(Node next) {
            this.next = next;
        }

        public Node getNext() {
            return next;
        }

        public RoomEntry getData() {
            return data;
        }

        public void setData(RoomEntry data) {
            this.data = data;
        }
    }

    private Node senthead=new Node();
    private Node tail;

    public CustomLinkedList(){
        tail = senthead;
    }

    /**
     * adds Room to path
     * @param data RoomEntry to be added
     */
    public void add(RoomEntry data){
        Node temp = new Node();
        temp.setData(data);
        tail.setNext(temp);
        tail = temp;
    }

    /**
     * Prints path of the maze that was captured
     */
    public void printList(){
        Node current = senthead.getNext();
        while(current!=null){
            System.out.print(current.getData().getID()+" ");
            current = current.getNext();
        }
        System.out.println();
    }
}
