package org.solace.util;

import java.util.ArrayList;
import org.solace.game.entity.mobile.Mobile;

/**
 * Container for a new type of list exclusive to Clients to prevent
 * mixing up different types of elements in the team event list
 * 
 * @author Faris
 */
public class MobileList<E extends Mobile> extends ArrayList<E> {

    // ALLOW USAGE OF A LIST WITH EXCLUSIVITY TO Client TYPES ONLY
    
}