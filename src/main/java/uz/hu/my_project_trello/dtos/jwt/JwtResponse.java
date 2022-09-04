package uz.hu.my_project_trello.dtos.jwt;


public record JwtResponse(
        String accessToken,
        String refreshToken,
        String tokenType) {
}
