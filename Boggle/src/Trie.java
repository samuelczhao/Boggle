
public class Trie
{
    public static final int R = 26;
    
    public static class Node 
    {
        public boolean val;
        public Node[] next = new Node[R];
    }
    
    public Node root;
    
    public boolean containsPrefix(String key)
	{
		return get(root, 0, key) != null;
	}
    public Node get(Node node, int a, String key) 
    {
        if (a == key.length()) return node;
        char c = find(key, a);
        return get(node.next[c], a + 1, key);
    }

    public void put(String key) 
    {
        root = put(root, 0, key);
    }
    
    public char find(String s, int a)
    {
        return (char) (s.charAt(a) - 'A');
    }
    
    public boolean containsWord(String key) 
    {
        Node node = get(root, 0, key);
        
        return node != null && node.val;
    }

    public Node put(Node node, int a, String key) 
    {
        if (node == null) 
        {
        	node = new Node();
        }
        
        if (a == key.length())
        {
            node.val = true;
            return node;
        }
        
        char c = find(key, a);
        node.next[c] = put(node.next[c], a + 1, key);
        return node;
    }

}
