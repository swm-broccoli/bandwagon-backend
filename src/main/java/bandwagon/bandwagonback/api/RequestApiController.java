package bandwagon.bandwagonback.api;

import bandwagon.bandwagonback.domain.Band;
import bandwagon.bandwagonback.domain.RequestType;
import bandwagon.bandwagonback.domain.User;
import bandwagon.bandwagonback.dto.RequestListDto;
import bandwagon.bandwagonback.dto.exception.PrerequisiteNotMetException;
import bandwagon.bandwagonback.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@Tag(name = "RequestApiController")
@Slf4j
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class RequestApiController {

    private final RequestService requestService;
    private final BandPrerequisiteService bandPrerequisiteService;
    private final UserService userService;
    private final BandService bandService;

    @Operation(description = "유저에게 온/밴드가 보낸 밴드 초대 요청 조회")
    @GetMapping("/api/request/invite")
    public ResponseEntity<?> getInviteRequests(@AuthenticationPrincipal UserDetails userDetails,  @RequestParam boolean sent) {
        String email = userDetails.getUsername();
        User user = userService.findOneByEmail(email);
        if (sent) {
            Band band = bandService.getUsersBand(user);
            RequestListDto requestListDto = requestService.getRequestOnBand(band, RequestType.INVITE);
            return ResponseEntity.ok(requestListDto);
        } else {
            RequestListDto requestListDto = requestService.getRequestOnUser(user, RequestType.INVITE);
            return ResponseEntity.ok(requestListDto);
        }
    }

    @Operation(description = "유저가 보낸/밴드에게 온 밴드 가입 요청 조회")
    @GetMapping("/api/request/apply")
    public ResponseEntity<?> getApplyRequests(@AuthenticationPrincipal UserDetails userDetails, @RequestParam boolean sent) {
        String email = userDetails.getUsername();
        User user = userService.findOneByEmail(email);
        if (sent) {
            RequestListDto requestListDto = requestService.getRequestOnUser(user, RequestType.APPLY);
            return ResponseEntity.ok(requestListDto);
        } else {
            Band band = bandService.getUsersBand(user);
            RequestListDto requestListDto = requestService.getRequestOnBand(band, RequestType.APPLY);
            return ResponseEntity.ok(requestListDto);
        }
    }

    @Operation(description = "밴드 초대 요청 보내기")
    @PostMapping("/api/request/invite")
    public ResponseEntity<?> sendInviteRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long userId) {
        String email = userDetails.getUsername();
        User invitingUser = userService.findOneByEmail(email);
        User invitedUser = userService.findOne(userId);
        requestService.sendInviteRequest(invitingUser, invitedUser);
        return ResponseEntity.ok(null);
    }

    @Operation(description = "밴드 초대 요청 응답하기")
    @PostMapping("/api/request/invite/{request_id}")
    public ResponseEntity<?> respondInviteRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("request_id") Long requestId, @RequestParam boolean accept) {
        String email = userDetails.getUsername();
        User respondingUser = userService.findOneByEmail(email);
        if (accept) {
            requestService.acceptInviteRequest(respondingUser, requestId);
        } else {
            requestService.declineInviteRequest(respondingUser, requestId);
        }
        return ResponseEntity.ok(null);
    }

    @Operation(description = "밴드 초대 요청 취소하기")
    @DeleteMapping("/api/request/invite/{request_id}")
    public ResponseEntity<?> cancelInviteRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("request_id") Long requestId) {
        String email = userDetails.getUsername();
        User cancelingUser = userService.findOneByEmail(email);
        requestService.cancelInviteRequest(cancelingUser, requestId);
        return ResponseEntity.ok(null);
    }

    @Operation(description = "밴드 가입 요청 보내기")
    @PostMapping("/api/request/apply")
    public ResponseEntity<?> sendApplyRequest(@AuthenticationPrincipal UserDetails userDetails, @RequestParam Long postId) {
        String email = userDetails.getUsername();
        User applyingUser = userService.findOneByEmail(email);
        if (!bandPrerequisiteService.canUserApply(email, postId)) {
            throw new PrerequisiteNotMetException();
        }
        requestService.sendApplyRequest(applyingUser, postId);
        return ResponseEntity.ok(null);
    }

    @Operation(description = "밴드 가입 요청 응답하기")
    @PostMapping("/api/request/apply/{request_id}")
    public ResponseEntity<?> respondApplyRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("request_id") Long requestId, @RequestParam boolean accept) {
        String email = userDetails.getUsername();
        User respondingUser = userService.findOneByEmail(email);
        if (accept) {
            requestService.acceptApplyRequest(respondingUser, requestId);
        } else {
            requestService.declineApplyRequest(respondingUser, requestId);
        }
        return ResponseEntity.ok(null);
    }

    @Operation(description = "밴드 가입 요청 취소하기")
    @DeleteMapping("/api/request/apply/{request_id}")
    public ResponseEntity<?> cancelApplyRequest(@AuthenticationPrincipal UserDetails userDetails, @PathVariable("request_id") Long requestId) {
        String email = userDetails.getUsername();
        User cancelingUser = userService.findOneByEmail(email);
        requestService.cancelApplyRequest(cancelingUser, requestId);
        return ResponseEntity.ok(null);
    }

}
