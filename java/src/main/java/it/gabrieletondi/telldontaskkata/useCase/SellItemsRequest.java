package it.gabrieletondi.telldontaskkata.useCase;

import java.util.List;
import java.util.stream.Stream;

public class SellItemsRequest {
    private List<SellItemRequest> requests;

    public void setRequests(List<SellItemRequest> requests) {
        this.requests = requests;
    }

    public List<SellItemRequest> getRequests() {
        return requests;
    }

    public Stream<SellItemRequest> stream() {
        return requests.stream();
    }
}
