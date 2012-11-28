package org.selfbus.sbhome.service;

import org.selfbus.sbhome.model.group.Group;

/**
 * Interface for listeners for group events.
 */
public interface GroupListener
{
   /**
    * The value of the group changed.
    *
    * @param group - the group
    */
   public void groupValueChanged(Group group);
}
