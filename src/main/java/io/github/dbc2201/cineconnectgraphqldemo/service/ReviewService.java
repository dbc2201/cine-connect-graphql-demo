package io.github.dbc2201.cineconnectgraphqldemo.service;

import io.github.dbc2201.cineconnectgraphqldemo.domain.Movie;
import io.github.dbc2201.cineconnectgraphqldemo.domain.ReactionTag;
import io.github.dbc2201.cineconnectgraphqldemo.domain.Review;
import io.github.dbc2201.cineconnectgraphqldemo.domain.User;
import io.github.dbc2201.cineconnectgraphqldemo.repository.MovieRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.ReviewRepository;
import io.github.dbc2201.cineconnectgraphqldemo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * Service layer for Review operations.
 * Handles business logic and transaction management.
 */
@Service
@Transactional(readOnly = true)
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final MovieRepository movieRepository;

    public ReviewService(ReviewRepository reviewRepository,
                         UserRepository userRepository,
                         MovieRepository movieRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
        this.movieRepository = movieRepository;
    }

    public Optional<Review> findById(Long id) {
        return reviewRepository.findById(id);
    }

    public Page<Review> findByMovieId(Long movieId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findByMovieId(movieId, pageable);
    }

    public Page<Review> findByUserId(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return reviewRepository.findByUserId(userId, pageable);
    }

    public Optional<BigDecimal> getAverageRatingForMovie(Long movieId) {
        return reviewRepository.findAverageRatingByMovieId(movieId);
    }

    public long getReviewCountForMovie(Long movieId) {
        return reviewRepository.countByMovieId(movieId);
    }

    @Transactional
    public Review createReview(Long userId, Long movieId, BigDecimal rating,
                               String content, boolean containsSpoiler,
                               Set<ReactionTag> reactionTags) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        Movie movie = movieRepository.findById(movieId)
            .orElseThrow(() -> new IllegalArgumentException("Movie not found: " + movieId));

        // Check if user already reviewed this movie
        if (reviewRepository.existsByUserIdAndMovieId(userId, movieId)) {
            throw new IllegalStateException("User has already reviewed this movie");
        }

        Review review = new Review(user, movie, rating);
        review.setContent(content);
        review.setContainsSpoiler(containsSpoiler);
        if (reactionTags != null) {
            review.setReactionTags(new HashSet<>(reactionTags));
        }

        return reviewRepository.save(review);
    }

    @Transactional
    public Optional<Review> updateReview(Long reviewId, BigDecimal rating,
                                         String content, Boolean containsSpoiler,
                                         Set<ReactionTag> reactionTags) {
        return reviewRepository.findById(reviewId).map(review -> {
            if (rating != null) review.setRating(rating);
            if (content != null) review.setContent(content);
            if (containsSpoiler != null) review.setContainsSpoiler(containsSpoiler);
            if (reactionTags != null) review.setReactionTags(new HashSet<>(reactionTags));

            return reviewRepository.save(review);
        });
    }

    @Transactional
    public boolean deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }
}
