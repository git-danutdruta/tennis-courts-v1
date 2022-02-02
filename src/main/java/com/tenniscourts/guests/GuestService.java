package com.tenniscourts.guests;

import java.util.List;

public interface GuestService {

    /**
     * Create new guest.
     *
     * @param guestDto -
     * @return GuestDTO having persisted data
     */
    GuestDTO createGuest(GuestDTO guestDto);

    /**
     * Update guest.
     *
     * @param guestDto -
     */
    GuestDTO updateGuest(GuestDTO guestDto);

    /**
     * Find guest by id.
     *
     * @param guestId guest identifier
     * @return GuestDTO
     */
    GuestDTO findGuestById(Long guestId);

    /**
     * Find all guests.
     *
     * @return List&lt;GuestDTO&gt;
     */
    List<GuestDTO> findAllGuest();

    /**
     * Find guest/guests by name. The method can return more than one record for one name.
     *
     * @param name guest name
     * @return List&lt;GuestDTO&gt;
     */
    List<GuestDTO> findGuestByName(String name);
}
