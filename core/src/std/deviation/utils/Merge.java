/**
 * @author Alexander J. Schmidt
 * @version 1.0
 * Array List Merger
 *
 */
package std.deviation.utils;

import java.util.ArrayList;

public class Merge
{

    /**
     * Merge sorts an array list based on the compare method of the items in the
     * list
     * 
     * @param list
     * @return
     */
    public static <T extends Comparable<? super T>> ArrayList<T> mergeSort(ArrayList<T> list)
    {
        if (list.isEmpty())
        {
            return list;
        }
        ArrayList<T> left = new ArrayList<T>();
        ArrayList<T> right = new ArrayList<T>();
        int center;

        if (list.size() == 1)
        {
            return list;
        }
        else
        {
            center = list.size() / 2;
            // copy the left half of whole into the left.
            for (int i = 0; i < center; i++)
            {
                left.add(list.get(i));
            }

            // copy the right half of whole into the new arraylist.
            for (int i = center; i < list.size(); i++)
            {
                right.add(list.get(i));
            }

            // Sort the left and right halves of the arraylist.
            left = mergeSort(left);
            right = mergeSort(right);

            // Merge the results back together.
            merge(left, right, list);
        }
        return list;
    }

    private static <T extends Comparable<? super T>> void merge(ArrayList<T> left,
            ArrayList<T> right, ArrayList<T> whole)
    {
        int leftIndex = 0;
        int rightIndex = 0;
        int wholeIndex = 0;

        // As long as neither the left nor the right ArrayList has
        // been used up, keep taking the smaller of left.get(leftIndex)
        // or right.get(rightIndex) and adding it at both.get(bothIndex).
        while (leftIndex < left.size() && rightIndex < right.size())
        {
            if ((left.get(leftIndex).compareTo(right.get(rightIndex))) < 0)
            {
                whole.set(wholeIndex, left.get(leftIndex));
                leftIndex++;
            }
            else
            {
                whole.set(wholeIndex, right.get(rightIndex));
                rightIndex++;
            }
            wholeIndex++;
        }

        ArrayList<T> rest;
        int restIndex;
        if (leftIndex >= left.size())
        {
            // The left ArrayList has been use up...
            rest = right;
            restIndex = rightIndex;
        }
        else
        {
            // The right ArrayList has been used up...
            rest = left;
            restIndex = leftIndex;
        }

        // Copy the rest of whichever ArrayList (left or right) was not used up.
        for (int i = restIndex; i < rest.size(); i++)
        {
            whole.set(wholeIndex, rest.get(i));
            wholeIndex++;
        }
    }
}
