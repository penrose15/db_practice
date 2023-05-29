package hello.jdbc.repository;

import hello.jdbc.domain.Members;

public interface MemberRepository {
    Members save(Members member);
    Members findById(String memberId);
    void update(String memberId, int money);
    void delete(String memberId);
}
