package structures;

import java.util.*;

/**
 * This class implements an HTML DOM Tree. Each node of the tree is a TagNode, with fields for
 * tag/text, first child and sibling.
 * 
 */
public class Tree {
	
	/**
	 * Root node
	 */
	TagNode root=null;
	
	/**
	 * Scanner used to read input HTML file when building the tree
	 */
	Scanner sc;
	
	/**
	 * Initializes this tree object with scanner for input HTML file
	 * 
	 * @param sc Scanner for input HTML file
	 */
	public Tree(Scanner sc) {
		this.sc = sc;
		root = null;
	}
	
	/**
	 * Builds the DOM tree from input HTML file, through scanner passed
	 * in to the constructor and stored in the sc field of this object. 
	 * 
	 * The root of the tree that is built is referenced by the root field of this object.
	 */
	public void build() {
		/** COMPLETE THIS METHOD **/
		String s = sc.nextLine();
		s = s.substring(1,s.indexOf(">"));
		root = new TagNode(s,null,null);
		TagNode ptr = root;
		Stack<Character> stack = new Stack<Character>();
		Stack<Character> reverseStack = new Stack<Character>();
		
		while(sc.hasNextLine()){
			s = sc.nextLine();
			
			//Return Back To Parent
			if(s.contains("</")) {
				
				//Remove Sibling Counters
				if(!stack.isEmpty()) {
					while(stack.peek().equals('s')) {
						stack.pop();
					}
					//Remove One Parent Counter To Return Up One Level
					stack.pop();
				}
				
				//Reverse The Stack To The Correct Order
				while(!stack.isEmpty()) {
					reverseStack.push(stack.pop());
				}
				
				//Transverse The Linked List
				ptr = root;
				while(!reverseStack.isEmpty()) {
					switch(reverseStack.peek()) {
						case 'c' : ptr = ptr.firstChild;
										 stack.push(reverseStack.pop());
										 break;
						case 's' : ptr = ptr.sibling;
										 stack.push(reverseStack.pop());
										 break;
					}
				}
				
			}
			
			//Add Tag, Stack keeps track of nested level
			else if(s.contains("<")) {
				s = s.substring(1,s.indexOf(">"));
				if(ptr.firstChild==null) {
					ptr.firstChild = new TagNode(s,null,null);
					stack.push('c');
					ptr = ptr.firstChild;
				}
				else {
					stack.push('c');
					ptr = ptr.firstChild;
					while(ptr.sibling!=null) {
						stack.push('s');
						ptr = ptr.sibling;
					}
					ptr.sibling = new TagNode(s,null,null);
					stack.push('s');
					ptr = ptr.sibling;
				}
				
			}
			
			//Add Text
			else {
				if(ptr.firstChild==null) {
					ptr.firstChild = new TagNode(s,null,null);
				}
				else {
					TagNode temp = ptr;
					ptr = ptr.firstChild;
					while(ptr.sibling!=null) {
						ptr = ptr.sibling;
					}
					ptr.sibling = new TagNode(s,null,null);
					ptr = temp;
				}
			}

		}
			
	}
	
	/**
	 * Replaces all occurrences of an old tag in the DOM tree with a new tag
	 * 
	 * @param oldTag Old tag
	 * @param newTag Replacement tag
	 */
	public void replaceTag(String oldTag, String newTag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		Stack<Character> stack = new Stack<Character>();
		Stack<Character> reverseStack = new Stack<Character>();
		stack.push('c');
		ptr = ptr.firstChild;
		while(!ptr.tag.equals("html")){
			if(ptr.tag.equals(oldTag)) {
				ptr.tag = newTag;
			}
			if(ptr.firstChild!=null) {
				stack.push('c');
				ptr = ptr.firstChild;
			}
			else if(ptr.sibling!=null) {
				stack.push('s');
				ptr = ptr.sibling;
			}
			else{				
				//Remove Sibling Counters
				if(!stack.isEmpty()) {
					while(stack.peek().equals('s')) {
						stack.pop();
					}
					//Remove One Parent Counter To Return Up One Level
					stack.pop();
				}
				
				//Reverse The Stack To The Correct Order
				while(!stack.isEmpty()) {
					reverseStack.push(stack.pop());
				}
				
				//Transverse The Linked List
				ptr = root;
				while(!reverseStack.isEmpty()) {
					switch(reverseStack.peek()) {
						case 'c' : ptr = ptr.firstChild;
										 stack.push(reverseStack.pop());
										 break;
						case 's' : ptr = ptr.sibling;
										 stack.push(reverseStack.pop());
										 break;
					}
				}
				if(ptr.sibling!=null) {
					stack.push('s');
					ptr = ptr.sibling;
				}
				else {
					while((ptr.sibling==null)&&!(ptr.tag.equals("html"))) {
						//Remove Sibling Counters
						if(!stack.isEmpty()) {
							while(stack.peek().equals('s')) {
								stack.pop();
							}
							//Remove One Parent Counter To Return Up One Level
							stack.pop();
						}
						
						//Reverse The Stack To The Correct Order
						while(!stack.isEmpty()) {
							reverseStack.push(stack.pop());
						}
						
						//Transverse The Linked List
						ptr = root;
						while(!reverseStack.isEmpty()) {
							switch(reverseStack.peek()) {
								case 'c' : ptr = ptr.firstChild;
												 stack.push(reverseStack.pop());
												 break;
								case 's' : ptr = ptr.sibling;
												 stack.push(reverseStack.pop());
												 break;
							}
						}
					}
					if(ptr.sibling!=null) {
						stack.push('s');
						ptr = ptr.sibling;
					}
				}
			}
						
		}
	}
	
	/**
	 * Boldfaces every column of the given row of the table in the DOM tree. The boldface (b)
	 * tag appears directly under the td tag of every column of this row.
	 * 
	 * @param row Row to bold, first row is numbered 1 (not 0).
	 */
	public void boldRow(int row) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root.firstChild;
		Stack<Character> stack = new Stack<Character>(); 
		while(!ptr.tag.equals("table")) {
			if(ptr.sibling!=null) {
				ptr=ptr.sibling;
			}
			else if(ptr.firstChild!=null){
				ptr=ptr.firstChild;
			}
		}
		for(int i=0; i<row; i++) {
			stack.push('c');
		}
		stack.pop();
		ptr = ptr.firstChild;
		while(!stack.isEmpty()) {
			stack.pop();
			ptr = ptr.sibling;
		}
		ptr = ptr.firstChild;
		TagNode col = ptr;
		ptr = col.firstChild;
		while(col!=null) {
			TagNode temp = new TagNode(ptr.tag,ptr.firstChild,ptr.sibling);
			ptr.tag = "b";
			ptr.firstChild = temp;
			col = col.sibling;
			if(col==null) {
				break;
			}
			ptr = col.firstChild;
			if(ptr==null) {
				break;
			}
		}
		
	}
	
	/**
	 * Remove all occurrences of a tag from the DOM tree. If the tag is p, em, or b, all occurrences of the tag
	 * are removed. If the tag is ol or ul, then All occurrences of such a tag are removed from the tree, and, 
	 * in addition, all the li tags immediately under the removed tag are converted to p tags. 
	 * 
	 * @param tag Tag to be removed, can be p, em, b, ol, or ul
	 */
	public void removeTag(String tag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		Stack<Character> stack = new Stack<Character>();
		Stack<Character> reverseStack = new Stack<Character>();
		stack.push('c');
		ptr = ptr.firstChild;
		while(!ptr.tag.equals("html")){
			if(ptr.tag.equals(tag)) {
				TagNode ptrTemp = ptr;
				TagNode sibTemp = ptr.sibling;
				TagNode temp = new TagNode(ptr.firstChild.tag,ptr.firstChild.firstChild,ptr.firstChild.sibling);
				if((tag.equals("ol"))||(tag.equals("ul"))) {
					ptr.tag = "p";
					ptr.firstChild = temp.firstChild;
					while(temp.sibling!=null) {
						ptr.sibling = temp.sibling;
						ptr = ptr.sibling;
						ptr.tag = "p";
						temp = temp.sibling;		
					}
					ptr.sibling = sibTemp;
					ptr = ptrTemp;
				}
				else {
					ptr.tag = temp.tag;
					ptr.firstChild = temp.firstChild;
					while(temp.sibling!=null) {
						ptr.sibling = temp.sibling;
						ptr = ptr.sibling;
						temp = temp.sibling;		
					}
					ptr.sibling = sibTemp;
					ptr = ptrTemp;	
				}
			}

			if(ptr.firstChild!=null) {
				stack.push('c');
				ptr = ptr.firstChild;
			}
			else if(ptr.sibling!=null) {
				stack.push('s');
				ptr = ptr.sibling;
			}
			else{				
				//Remove Sibling Counters
				if(!stack.isEmpty()) {
					while(stack.peek().equals('s')) {
						stack.pop();
					}
					//Remove One Parent Counter To Return Up One Level
					stack.pop();
				}
				
				//Reverse The Stack To The Correct Order
				while(!stack.isEmpty()) {
					reverseStack.push(stack.pop());
				}
				
				//Transverse The Linked List
				ptr = root;
				while(!reverseStack.isEmpty()) {
					switch(reverseStack.peek()) {
						case 'c' : ptr = ptr.firstChild;
										 stack.push(reverseStack.pop());
										 break;
						case 's' : ptr = ptr.sibling;
										 stack.push(reverseStack.pop());
										 break;
					}
				}
				if(ptr.sibling!=null) {
					stack.push('s');
					ptr = ptr.sibling;
				}
				else {
					while((ptr.sibling==null)&&!(ptr.tag.equals("html"))) {
						//Remove Sibling Counters
						if(!stack.isEmpty()) {
							while(stack.peek().equals('s')) {
								stack.pop();
							}
							//Remove One Parent Counter To Return Up One Level
							stack.pop();
						}
						
						//Reverse The Stack To The Correct Order
						while(!stack.isEmpty()) {
							reverseStack.push(stack.pop());
						}
						
						//Transverse The Linked List
						ptr = root;
						while(!reverseStack.isEmpty()) {
							switch(reverseStack.peek()) {
								case 'c' : ptr = ptr.firstChild;
												 stack.push(reverseStack.pop());
												 break;
								case 's' : ptr = ptr.sibling;
												 stack.push(reverseStack.pop());
												 break;
							}
						}
					}
					if(ptr.sibling!=null) {
						stack.push('s');
						ptr = ptr.sibling;
					}
				}
			}
		}
		
	}
	
	/**
	 * Adds a tag around all occurrences of a word in the DOM tree.
	 * 
	 * @param word Word around which tag is to be added
	 * @param tag Tag to be added
	 */
	public void addTag(String word, String tag) {
		/** COMPLETE THIS METHOD **/
		TagNode ptr = root;
		Stack<Character> stack = new Stack<Character>();
		Stack<Character> reverseStack = new Stack<Character>();
		stack.push('c');
		ptr = ptr.firstChild;
		boolean forceExit = false;
		word = word.toLowerCase();
		
		while(!ptr.tag.equals("html")){
			String changeTag = ptr.tag.toLowerCase();
			if( 
				(changeTag.contains(word)) && 
				(
					(changeTag.indexOf(word)==0)||
					(changeTag.charAt(changeTag.indexOf(word)-1)==' ')
				) && 
				(
					(changeTag.indexOf(word)+word.length()==changeTag.length())||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())==' ')||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())=='.')||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())==',')||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())=='?')||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())=='!')||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())==':')||
					(changeTag.charAt(changeTag.indexOf(word)+word.length())==';')
				)
			  ) {
				String s = ptr.tag;
				int wLength = word.length();
				if(changeTag.indexOf(word)+word.length()!=s.length()) {
					Character c = s.charAt(changeTag.indexOf(word)+word.length());
					switch(c) {
						case '.':
						case ',':
						case '?':
						case '!':
						case ';':
						case ':': wLength = word.length()+1;
								  break;
						default: wLength = word.length();
					}
				}
				String stringBefore = s.substring(0,changeTag.indexOf(word));
				String stringNew = s.substring(changeTag.indexOf(word),changeTag.indexOf(word)+wLength);
				String stringAfter	= s.substring(changeTag.indexOf(word)+wLength);

				if(stringBefore.length()!=0) {
					ptr.tag = stringBefore;
					ptr.sibling = new TagNode(tag,null,null);
					ptr = ptr.sibling;
				}
				else {
					ptr.tag = tag;

				}
				ptr.firstChild = new TagNode(stringNew,null,null);
				
				if(stringAfter.length()!=0) {
					ptr.sibling = new TagNode(stringAfter,null,null);
					ptr = ptr.sibling;
				}
				else {
					forceExit = true;
				}
			}
			if((ptr.firstChild!=null)&&!(forceExit)) {
				stack.push('c');
				ptr = ptr.firstChild;
				
			}
			else if((ptr.sibling!=null)&&!(forceExit)) {
				stack.push('s');
				ptr = ptr.sibling;
			}
			else{				
				//Remove Sibling Counters
				if(!stack.isEmpty()) {
					while(stack.peek().equals('s')) {
						stack.pop();
					}
					//Remove One Parent Counter To Return Up One Level
					stack.pop();
				}
				
				//Reverse The Stack To The Correct Order
				while(!stack.isEmpty()) {
					reverseStack.push(stack.pop());
				}
				
				//Transverse The Linked List
				ptr = root;
				while(!reverseStack.isEmpty()) {
					switch(reverseStack.peek()) {
						case 'c' : ptr = ptr.firstChild;
										 stack.push(reverseStack.pop());
										 break;
						case 's' : ptr = ptr.sibling;
										 stack.push(reverseStack.pop());
										 break;
					}
				}
				if(ptr.sibling!=null) {
					stack.push('s');
					ptr = ptr.sibling;
				}
				else {
					while((ptr.sibling==null)&&!(ptr.tag.equals("html"))) {
						//Remove Sibling Counters
						if(!stack.isEmpty()) {
							while(stack.peek().equals('s')) {
								stack.pop();
							}
							//Remove One Parent Counter To Return Up One Level
							stack.pop();
						}
						
						//Reverse The Stack To The Correct Order
						while(!stack.isEmpty()) {
							reverseStack.push(stack.pop());
						}
						
						//Transverse The Linked List
						ptr = root;
						while(!reverseStack.isEmpty()) {
							switch(reverseStack.peek()) {
								case 'c' : ptr = ptr.firstChild;
												 stack.push(reverseStack.pop());
												 break;
								case 's' : ptr = ptr.sibling;
												 stack.push(reverseStack.pop());
												 break;
							}
						}
					}
					if(ptr.sibling!=null) {
						stack.push('s');
						ptr = ptr.sibling;
					}
				}
				forceExit = false;
			}
						
		}
	}
	
	/**
	 * Gets the HTML represented by this DOM tree. The returned string includes
	 * new lines, so that when it is printed, it will be identical to the
	 * input file from which the DOM tree was built.
	 * 
	 * @return HTML string, including new lines. 
	 */
	public String getHTML() {
		StringBuilder sb = new StringBuilder();
		getHTML(root, sb);
		return sb.toString();
	}
	
	private void getHTML(TagNode root, StringBuilder sb) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			if (ptr.firstChild == null) {
				sb.append(ptr.tag);
				sb.append("\n");
			} else {
				sb.append("<");
				sb.append(ptr.tag);
				sb.append(">\n");
				getHTML(ptr.firstChild, sb);
				sb.append("</");
				sb.append(ptr.tag);
				sb.append(">\n");	
			}
		}
	}
	
	/**
	 * Prints the DOM tree. 
	 *
	 */
	public void print() {
		print(root, 1);
	}
	
	private void print(TagNode root, int level) {
		for (TagNode ptr=root; ptr != null;ptr=ptr.sibling) {
			for (int i=0; i < level-1; i++) {
				System.out.print("      ");
			};
			if (root != this.root) {
				System.out.print("|---- ");
			} else {
				System.out.print("      ");
			}
			System.out.println(ptr.tag);
			if (ptr.firstChild != null) {
				print(ptr.firstChild, level+1);
			}
		}
	}
}
