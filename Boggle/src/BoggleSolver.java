import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashSet;

public class BoggleSolver
{
	private Trie tDictionary;
	private BoggleBoard currBoard;

	public BoggleSolver(String[] dictionary)
	{
		tDictionary = new Trie();

		for (String word : dictionary)
		{
			tDictionary.put(word);
		}
	}

	public Iterable<String> getAllValidWords(BoggleBoard board)
	{
		HashSet<String> validWords = new HashSet<>();

		currBoard = board;

		for (int i = 0; i < board.rows(); i++)
		{
			for (int j = 0; j < board.cols(); j++)
			{
				boolean[][] traversed = new boolean[board.rows()][board.cols()];
				traversed[i][j] = true;

				getWords(i, j, "", validWords, traversed, tDictionary.root);
			}
		}

		return validWords;
	}

	//s
	public int scoreOf(String word)
	{
		int length = word.length();

		if (length <= 2)
		{
			return 0;
		}
		else if (length <= 4)
		{
			return 1;
		}
		else if (length <= 5)
		{
			return 2;
		}
		else if (length <= 6)
		{
			return 3;
		}
		else if (length <= 7)
		{
			return 5;
		}
		else
		{
			return 11;
		}
	}

	private void getWords(int row, int column, String prefix, HashSet<String> validWords, boolean[][] traversed,
			Trie.Node n)
	{
		String add = "" + currBoard.getLetter(row, column);
		if (currBoard.getLetter(row, column) == 'Q')
		{
			add += "U";
		}
		String newPrefix = prefix + add;

		traversed[row][column] = true;
		Trie.Node node = tDictionary.get(n, prefix.length(), newPrefix);
		if (node != null && node.val && newPrefix.length() >= 3)
		{
			validWords.add(newPrefix);
		}

		if (node != null)
		{
			for (int i = row - 1; i <= row + 1; i++)
			{
				for (int j = column - 1; j <= column + 1; j++)
				{
					if (isValid(i, j) && !traversed[i][j])
					{
						getWords(i, j, newPrefix, validWords, traversed, node);
					}
				}
			}
		}
		traversed[row][column] = false;
	}

	private boolean isValid(int row, int col)
	{
		return row >= 0 && row < currBoard.rows() && col >= 0 && col < currBoard.cols();
	}

	// ------------------------
	// FREE TEST CODE
	// ------------------------

	public static void main(String[] args)
	{
		In in = new In("testinput/dictionary-common.txt");
		String[] dictionary = in.readAllStrings();
		BoggleSolver solver = new BoggleSolver(dictionary);

		// You can use exactly one of these (and comment out the other).
		//
		// Use the first one to test against a single board.
		//
		// Use the second one to test against all the boards at once. 

		// Example 1: Run with a single board
		mainWithOneBoardFile(solver, "testinput/board-4x4.txt");

		// Example 2: Run with ALL boards.  If you use this, you should only use
		// dictionary-common.txt as your dictionary (above), as this will
		// print passed or failed based on whether
		// the scores you returned match the filename.
		// mainWithAllBoardFiles(solver);
	}

	private static void mainWithOneBoardFile(BoggleSolver solver, String boardFile)
	{
		BoggleBoard board = new BoggleBoard(boardFile);
		int score = 0;
		for (String word : solver.getAllValidWords(board))
		{
			StdOut.print(word + " ");
			score += solver.scoreOf(word);
		}
		StdOut.println("\nScore = " + score);
	}

	private static void mainWithAllBoardFiles(BoggleSolver solver)
	{

		Path dir = FileSystems.getDefault().getPath("testinput");

		DirectoryStream<Path> ds;

		try
		{
			ds = Files.newDirectoryStream(dir);
		}
		catch (Exception e)
		{
			// This is awful error handling.  Never do this in your job.
			return;
		}

		for (Path file : ds)
		{
			String filenameString = file.getFileName().toString().toLowerCase();
			if (!filenameString.startsWith("board-points."))
				continue;

			String expectedPointsStr = filenameString.split("\\.")[1];
			int expectedPoints = Integer.parseInt(expectedPointsStr);

			String filepathString = file.toString();
			StdOut.print(filepathString + ":");
			BoggleBoard board = new BoggleBoard(filepathString);
			int score = 0;
			ArrayList<String> words = new ArrayList<String>();
			for (String word : solver.getAllValidWords(board))
			{
				words.add(word);
				score += solver.scoreOf(word);
			}
			String successMsg = (expectedPoints == score) ? ".  passed"
					: ".  ***** FAILED *****\nWords found:\n" + words + "\n\n";
			StdOut.println(": Score = " + score + successMsg);
		}

	}
}
