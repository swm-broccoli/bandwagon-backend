package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.ErrorResponse;
import bandwagon.bandwagonback.jwt.JwtUtil;
import bandwagon.bandwagonback.service.BandPrerequisiteService;
import bandwagon.bandwagonback.service.RequestService;
import bandwagon.bandwagonback.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Tag(name = "RequestApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RequestApiController {

    private final RequestService requestService;
    private final BandPrerequisiteService bandPrerequisiteService;
    private final UserService userService;
    private final JwtUtil jwtTokenUtil;

    @Operation(description = "밴드 초대 요청 보내기")
    @PostMapping("/api/request/invite")
    public ResponseEntity<?> sendInviteRequest(@RequestParam Long userId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            User invitingUser = userService.findOneByEmail(email);
            User invitedUser = userService.findOne(userId);
            requestService.sendInviteRequest(invitingUser, invitedUser);
            // ADD NOTIFICATION LOGIC HERE
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 초대 요청 응답하기")
    @PostMapping("/api/request/invite/{request_id}")
    public ResponseEntity<?> respondInviteRequest(@PathVariable("request_id") Long requestId, @RequestParam boolean accept, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            User respondingUser = userService.findOneByEmail(email);
            if (accept) {
                requestService.acceptInviteRequest(respondingUser, requestId);
            } else {
                requestService.declineInviteRequest(respondingUser, requestId);
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 초대 요청 취소하기")
    @DeleteMapping("/api/request/invite/{request_id}")
    public ResponseEntity<?> cancelInviteRequest(@PathVariable("request_id") Long requestId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            User cancelingUser = userService.findOneByEmail(email);
            requestService.cancelInviteRequest(cancelingUser, requestId);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 가입 요청 보내기")
    @PostMapping("/api/request/apply")
    public ResponseEntity<?> sendApplyRequest(@RequestParam Long postId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            User applyingUser = userService.findOneByEmail(email);
            if (!bandPrerequisiteService.canUserApply(email, postId)) {
                throw new Exception("User does not meet prerequisites!");
            }
            requestService.sendApplyRequest(applyingUser, postId);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 가입 요청 응답하기")
    @PostMapping("/api/request/apply/{request_id}")
    public ResponseEntity<?> respondApplyRequest(@PathVariable("request_id") Long requestId, @RequestParam boolean accept, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            User respondingUser = userService.findOneByEmail(email);
            if (accept) {
                requestService.acceptApplyRequest(respondingUser, requestId);
            } else {
                requestService.declineApplyRequest(respondingUser, requestId);
            }
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    @Operation(description = "밴드 가입 요청 취소하기")
    @DeleteMapping("/api/request/apply/{request_id}")
    public ResponseEntity<?> cancelApplyRequest(@PathVariable("request_id") Long requestId, HttpServletRequest request) {
        String jwt = getJwtFromHeader(request);
        String email = jwtTokenUtil.extractUsername(jwt);
        try {
            User cancelingUser = userService.findOneByEmail(email);
            requestService.cancelApplyRequest(cancelingUser, requestId);
            return ResponseEntity.ok(null);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorResponse(e.getMessage()));
        }
    }

    private String getJwtFromHeader(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        return authorizationHeader.substring(7);
    }
}
