package my.app.myexpandablerecyclerview.data;

import java.util.List;

/* flat position - Flat list position, the position of an item relative to all the
 * other *visible* items on the screen. For example, if you have a three groups, each with
 * 2 children and all are collapsed, the "flat position" of the last group would be 2. And if
 * the first of those three groups was expanded, the flat position of the last group would now be 4.

 */

public class ExpandableList {


    //List of ExpandebleGrups
    public List<? extends ExpandableGroup> groups;

    //tells if a grup is expanded(true)
    public boolean[] expandedGroupIndexes;

    public ExpandableList(List<? extends ExpandableGroup> groups) {
        this.groups = groups;


        //all items are unexpanded
        expandedGroupIndexes = new boolean[groups.size()];
        for (int i = 0; i < groups.size(); i++) {
            expandedGroupIndexes[i] = false;
        }
    }



    //grup index ExpandebleGrupa
    //if Collapsed returs 1 for header if not,item +1

    private int numberOfVisibleItemsInGroup(int group) {
        if (expandedGroupIndexes[group]) {
            return groups.get(group).getItemCount() + 1;
        } else {
            return 1;
        }
    }


    //get total number of visibleChild item's
    public int getVisibleItemCount() {
        int count = 0;
        for (int i = 0; i < groups.size(); i++) {
            count += numberOfVisibleItemsInGroup(i);
        }
        return count;
    }


    /*translates flat list position to
      a)grup position if the flatList position corresponds to grup
      b)childPos if corresonds to Child
   */


    public ExpandableListPosition getUnflattenedPosition(int flPos)
    {
        int groupItemCount;
        int adapted = flPos;
        for (int i = 0; i < groups.size(); i++) {
            groupItemCount = numberOfVisibleItemsInGroup(i);
            if (adapted == 0) {
                return ExpandableListPosition.obtain(ExpandableListPosition.GROUP, i, -1, flPos);
            } else if (adapted < groupItemCount) {
                return ExpandableListPosition.obtain(ExpandableListPosition.CHILD, i, adapted - 1, flPos);
            }
            adapted -= groupItemCount;
        }
        throw new RuntimeException("Unknown state");
    }



    public int getFlattenedGroupIndex(ExpandableListPosition listPosition) {
        int groupIndex = listPosition.groupPos;
        int runningTotal = 0;
        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }


    //grupIndex represents index of a group
    public int getFlattenedGroupIndex(int groupIndex) {
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }

    public int getFlattenedGroupIndex(ExpandableGroup group) {
        int groupIndex = groups.indexOf(group);
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal;
    }



    public int getFlattenedChildIndex(long packedPosition) {
        ExpandableListPosition listPosition = ExpandableListPosition.obtainPosition(packedPosition);
        return getFlattenedChildIndex(listPosition);
    }




    public int getFlattenedChildIndex(ExpandableListPosition listPosition) {
        int groupIndex = listPosition.groupPos;
        int childIndex = listPosition.childPos;
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal + childIndex + 1;
    }


    public int getFlattenedChildIndex(int groupIndex, int childIndex) {
        int runningTotal = 0;

        for (int i = 0; i < groupIndex; i++) {
            runningTotal += numberOfVisibleItemsInGroup(i);
        }
        return runningTotal + childIndex + 1;
    }

    /**
     * @param groupIndex The index of a group within {@link #groups}
     * @return The flat list position for the first child in a group
     */
    public int getFlattenedFirstChildIndex(int groupIndex) {
        return getFlattenedGroupIndex(groupIndex) + 1;
    }

    public int getExpandableGroupItemCount(ExpandableListPosition listPosition) {
        return groups.get(listPosition.groupPos).getItemCount();
    }

    public ExpandableGroup getExpandableGroup(ExpandableListPosition listPosition) {
        return groups.get(listPosition.groupPos);
    }







}