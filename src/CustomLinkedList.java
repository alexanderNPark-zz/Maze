public class CustomLinkedList{

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

    public void add(RoomEntry data){
        Node temp = new Node();
        temp.setData(data);
        tail.setNext(temp);
        tail = temp;
    }

    public void printList(){
        Node current = senthead.getNext();
        while(current!=null){
            System.out.print(current.getData().getID()+" ");
            current = current.getNext();
        }
        System.out.println();
    }
}
