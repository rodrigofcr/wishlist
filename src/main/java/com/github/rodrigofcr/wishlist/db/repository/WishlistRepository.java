package com.github.rodrigofcr.wishlist.db.repository;

import com.github.rodrigofcr.wishlist.db.entity.Wishlist;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WishlistRepository extends MongoRepository<Wishlist, String> {

}
