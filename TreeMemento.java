import java.util.Stack;

class TreeMemento {
	private Stack<AbsTree> state = new Stack<AbsTree>();

	public void set_state(AbsTree t) {
		AbsTree absTree = null;
		if(t!=null) {
			absTree = t.clone();
		}
		state.push(absTree);		
	}

	public AbsTree get_state() {
		AbsTree absTree = null;
		if(!is_empty()) {
			absTree = state.pop();
		}
		return absTree;
	}
	
	public void clear() {
		state.clear();
	}
	
	boolean is_empty() { 
		return state.isEmpty();
	}
}
