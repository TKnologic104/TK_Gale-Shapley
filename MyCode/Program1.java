/*
 * Name: <your name>
 * EID: <your EID>
 */

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your solution goes in this class.
 * 
 * Please do not modify the other files we have provided for you, as we will use
 * our own versions of those files when grading your project. You are
 * responsible for ensuring that your solution works with the original version
 * of all the other files we have provided for you.
 * 
 * That said, please feel free to add additional files and classes to your
 * solution, as you see fit. We will use ALL of your additional files when
 * grading your solution.
 */
public class Program1 extends AbstractProgram1 {
    /**
     * Determines whether a candidate Matching represents a solution to the
     * Stable Marriage problem. Study the description of a Matching in the
     * project documentation to help you with this.
     */
    public boolean isStableMatching(Matching marriage) {
    	int couples = marriage.getWomenCount();
    	int pair_woman_idx = 0;
    	int pair_man_idx = 0;
    	int pair_womans_man_priority = 0;
        ArrayList<Integer> women_matching_list = marriage.getWomenMatching();
        ArrayList<Integer> woman_preference_list;
        ArrayList<Integer> man_preference_list;

    	for (; pair_woman_idx < couples; pair_woman_idx++){
//    		System.out.println("idx1" + pair_woman_idx);
    		pair_man_idx = women_matching_list.get(pair_woman_idx);
    		woman_preference_list = marriage.getWomenPreference().get(pair_woman_idx);
    		pair_womans_man_priority = woman_preference_list.get(pair_man_idx);
    		int woman_preference_idx = 0;
    		for (;woman_preference_idx < couples;woman_preference_idx++){
//        		System.out.println("idx2" + woman_preference_idx);
    			if (woman_preference_list.get(woman_preference_idx) < pair_womans_man_priority){
    				man_preference_list = marriage.getMenPreference().get(woman_preference_idx);
    				int pair2_mans_woman = 0;
    				for (;pair2_mans_woman < couples;pair2_mans_woman++){
//    	        		System.out.println("idx3" + pair2_mans_woman);
    					if (women_matching_list.get(pair2_mans_woman) == woman_preference_idx){
    						break;
    					}
    				}
    				if (man_preference_list.get(woman_preference_idx) < man_preference_list.get(pair2_mans_woman)){
    					return false;
    				}
    			}
    		}
    	}
    	return true;
    }
    /**
     * Determines a solution to the Stable Marriage problem from the given input
     * set. Study the project description to understand the variables which
     * represent the input to your solution.
     * 
     * @return A stable Matching.
     */
    public Matching stableMarriageGaleShapley(Matching marriage) {
        int couple_count = marriage.getWomenCount();
        ArrayList<Integer> women_matching_list = new ArrayList<Integer>(couple_count);
        ArrayList<Integer> woman_preference_list = new ArrayList<Integer>(couple_count);
        ArrayList<Integer> man_preference_list = new ArrayList<Integer>(couple_count);
        int woman_idx = 0;
    	int man_paired_idx = 0;
    	int highest_priority = couple_count;

    	for (int i = 0;i < couple_count;i++){
    		women_matching_list.add(i, -1);
    	}
    	boolean woman_free = true;
    	do {
    		woman_free = false;
    		/* find the first free woman in the match list */
        	for (woman_idx = 0;woman_idx < couple_count;woman_idx++){
        		if (women_matching_list.get(woman_idx) == -1){
        			break;
        		}
        	}
        	/* get the preference list of that free woman */
    		woman_preference_list = marriage.getWomenPreference().get(woman_idx);
    		/* pair that woman with first man of highest priority in her preference list */
    		highest_priority = couple_count + 1;
    		for (int i = 0; i < couple_count; i++){
	    		if (woman_preference_list.get(i) < highest_priority){
	    			highest_priority = woman_preference_list.get(i);
	    			man_paired_idx = i;
	    		}
	    	}
			women_matching_list.set(woman_idx, man_paired_idx);
    		/* if that man is already paired with some woman then find that woman and free her */
	    	for (int i = 0; i < couple_count; i++){
	    		if (i != woman_idx &&
	    			women_matching_list.get(i) == man_paired_idx){
	    			women_matching_list.set(i, -1);
	    		}
	    	}
	    	/* disable all women of lower or same priority than the woman this man is now paired with*/
    		man_preference_list = marriage.getMenPreference().get(man_paired_idx);
	    	for (int i = 0; i < couple_count; i++){
	    		if ((man_preference_list.get(i) >= man_preference_list.get(woman_idx)) &&
	    				( i != woman_idx)){
	    			man_preference_list.set(i, couple_count + 1);
	    	    	/* disable this man in the preference lists of those women who still have a higher priority 
	    	    	 * than the woman he is paired with now */
	    			woman_preference_list = marriage.getWomenPreference().get(i);
		    		woman_preference_list.set(man_paired_idx, couple_count + 1);
	    		}
	    	}
	    	for (int i = 0; i < couple_count; i++){
        		if (women_matching_list.get(i) == -1){
        			woman_free = true;
        		}
	    	}
    	} while (woman_free) ;
    	marriage.setWomanMatching(women_matching_list);
        return marriage; 
    }
}
