package info.nfuture.srre.simple_ruby_editor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.DocumentCommand;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextUtilities;

public class SimpleRubyAutoEditStrategy implements IAutoEditStrategy {
//	protected int tabWidth = 4;
	
	protected SimpleRubySourceViewer viewer;
	
	public SimpleRubyAutoEditStrategy( SimpleRubySourceViewer viewer) {
		this.viewer = viewer;
	}
	
	public void customizeDocumentCommand(IDocument document,
			DocumentCommand command) {

		String contentType = document.getDocumentPartitioner().getContentType(command.offset);
		if (IDocument.DEFAULT_CONTENT_TYPE.equals(contentType) && command.text != null) {
			int next = -1;
			if (command.offset < document.getLength()) {
				try {
					next = document.getChar(command.offset);
				} catch (BadLocationException e) {
					e.printStackTrace();
				}
			}
			if (command.text.equals("{")) {
				command.text = command.text + "}";
				command.caretOffset = command.offset + 1;
				command.shiftsCaret = false;
				command.doit = false;
				setNextChar( '}' );
			} else if (command.text.equals("}") && getNextChar() == '}' ) {
				if (next == '}') {
					command.text = "";
					command.caretOffset = command.offset + 1;
					command.shiftsCaret = false;
					command.doit = false;
				}
				setNextChar( -1 );
			} else if (command.text.equals("(")) {
				command.text = command.text + ")";
				command.caretOffset = command.offset + 1;
				command.shiftsCaret = false;
				command.doit = false;
				setNextChar( ')' );
			} else if (command.text.equals(")") && getNextChar() == ')') {
				if (next == ')') {
					command.text = "";
					command.caretOffset = command.offset + 1;
					command.shiftsCaret = false;
					command.doit = false;
				}
				setNextChar( -1 );
			} else if (command.text.equals("[")) {
				command.text = command.text + "]";
				command.caretOffset = command.offset + 1;
				command.shiftsCaret = false;
				command.doit = false;
				setNextChar( ']' );
			} else if (command.text.equals("]") && getNextChar() == ']') { 
				if (next == ']') {
					command.text = "";
					command.caretOffset = command.offset + 1;
					command.shiftsCaret = false;
					command.doit = false;
				}
				setNextChar( -1 );
			} else if (command.text.length()>0) {
				int delim_pos = TextUtilities.endsWith(document.getLegalLineDelimiters(), command.text); 
				if ( delim_pos != -1) {
					String [] delims = document.getLegalLineDelimiters();
					String delim = delims[delim_pos];
					try {
						int p = ( command.offset == document.getLength() ? command.offset - 1 : command.offset);
						if (p>=0) {
							IRegion info = document.getLineInformationOfOffset(p);
							int start = info.getOffset();
							int end= findEndOfWhiteSpace(document, start, command.offset);
	
							StringBuffer buf = new StringBuffer(command.text);
							
							String v = document.get(start,command.offset - start);
							boolean flag_match = false;
	
							if (!flag_match) {
								flag_match = addEnd(document,command,v,false,"def",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,false,"do",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,true,"if",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,true,"unless",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,false,"begin",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,false,"case",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,false,"class",start,end,delim,buf);
							}
							if (!flag_match) {
								flag_match = addEnd(document,command,v,false,"module",start,end,delim,buf);
							}
							if (!flag_match) {
								if (end > start) {
									buf.append(document.get(start, end - start));
								}
								command.text= buf.toString();
							}
						}
					} catch (BadLocationException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	protected boolean addEnd(IDocument document,DocumentCommand command,String v,boolean flagIf,String cmd,int start,int end,String delim,StringBuffer buf)
		throws BadLocationException {
		String patternStr = "(^|\\s)"+cmd + "($|\\s)";
		if (flagIf) {
			patternStr = "(^\\s*)"+cmd + "($|\\s)";
		}
		
		
		Pattern pattern = Pattern.compile(patternStr);
		Matcher matcher = pattern.matcher(v);
		if (matcher.find()) {
			String other = v.substring( matcher.end() );
			Pattern pattern2 = Pattern.compile("end($|\\s)");
			Matcher matcher2 = pattern2.matcher(other);
			if (!matcher2.find() && checkBraces(other) &&
					( (! flagIf) || (! checkLastOperator(other)) ) ) {
				
				String indent = "";
				if (end > start) {
					indent = document.get(start, end - start);
					buf.append(indent);
				}
				if (viewer.getTabSetting().isTabsToSpacesConversionEnabled()) {
					for (int ii=0;ii<viewer.getTabSetting().getTabWidth();ii++) {
						buf.append(" ");
					}
				} else {
					buf.append("\t");
				}
				buf.append(delim);
				if (end > start) {
					buf.append(indent);
				}
				buf.append("end");
				if (viewer.getTabSetting().isTabsToSpacesConversionEnabled()) {
					command.caretOffset = command.text.length() + command.offset + indent.length()
					+ viewer.getTabSetting().getTabWidth();
				} else {
					command.caretOffset = command.text.length() + command.offset + indent.length() + 1;
				}
				command.text = buf.toString();
				command.shiftsCaret = false;
				command.doit = false;
				return true;
			}
		}
		return false;
	}
	
	//  DefaultIndentLineAutoEditStrategyÇÃÇÇ‹ÇÈÇ‹ÇÈÉRÉsÅ[
	protected int findEndOfWhiteSpace(IDocument document, int offset, int end) throws BadLocationException {
		while (offset < end) {
			char c= document.getChar(offset);
			if (c != ' ' && c != '\t') {
				return offset;
			}
			offset++;
		}
		return end;
	}
	
	public int getNextChar() {
		return viewer.getNextChar();
	}
	
	public void setNextChar(int c) {
		viewer.setNextChar(c);
	}
	
	protected boolean checkBraces(String target) {
		if (target == null) {
			return true;
		}
		int p = 0;
		for (int i=0;i<target.length();i++) {
			char c = target.charAt(i);
			if (c == '(' || c == '{' || c == '[') {
				p ++;
			} else if ( c == ')' || c == '}' || c == ']') {
				p --;
			}
		}
		if (p==0) {
			return true;
		}
		return false;
	}

	protected boolean checkLastOperator(String target) {
		Pattern pattern = Pattern.compile("(&&|\\|\\||\\+|-|\\*|\\/|\\%)\\s*$");
		Matcher matcher = pattern.matcher(target);
		return matcher.find();
	}


}
