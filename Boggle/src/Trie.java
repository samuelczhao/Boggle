public class Trie
{
	private static final int R = 26;        // extended ASCII

    private Node root = new Node();

    public static class Node {
        private int val;
        public Node[] next = new Node[R];
    }
    
    public Trie()
    {
    	
    }
    
   /****************************************************
    * Is the key in the symbol table?
    ****************************************************/
    public boolean contains(String key) {
        return get(key) != null;
    }
    
    public Integer get(String key) {
        Node x = get(root, key, 0);
        if (x == null) return null;
        return x.val;
    }

    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c], key, d+1);
    }

    public void put(String key, int val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, int val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c] = put(x.next[c], key, val, d+1);
        return x;
    }

    public boolean containsPrefix(String prefix) {
        Node x = get(root, prefix, 0);
        for (Node node : x.next)
        {
        	if (node != null)
        	{
        		return true;
        	}
        }
        
        return false;
    }
}
