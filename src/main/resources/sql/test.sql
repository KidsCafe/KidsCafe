# select c.id,
#        c.name,
#        c.size,
#        avg(re.star) as star,
#        count(re.id) as count,
#        case when count(ro.id) > 0 then true else false end as roomExist,
#        c.parking,
#        c.restaurant,
#        c.opened_at,
#        c.closed_at
# from cafe as c left join review as re on c.id = re.cafe_id
#      left join room as ro on c.id = ro.cafe_id
#      left join fee as f on c.id = f.cafe_id
# where star >= 2.0 and star < 3.0
# group by c.id
# order by star desc;

use
    kidscafe;

select count(id)
from room
group by cafe_id;

select *
from cafe
where id = 81;
# 목, 일, 수, 토
select c.id,
       c.name,
       c.address,
       c.size,
       avg(re.star),
       COUNT(DISTINCT re.id),
       case
           when (COUNT(DISTINCT ro.id) >= 1)
               then abs(sign(1))
           else false
           end,
       c.parking,
       c.restaurant,
       c.opened_at,
       c.closed_at
from cafe c
         left join review re on re.cafe_id = c.id
         left join room ro on ro.cafe_id = c.id
         left join fee f on f.cafe_id = c.id
where c.size <= 500000
  and c.parking = false
  and (
    c.day_off like '%일%' escape '!'
        and c.opened_at >= '00:00'
        and c.opened_at <= '23:59'
    )
  and c.restaurant = true
  and c.multi_family = false
  and c.opened_at >= '00:00'
  and c.opened_at <= '01:30'
group by c.id
having avg(re.star) between 0.0 and 5.0
order by avg(re.star);
# limit ?, ?

select c.id,
       c.name,
       c.address,
       c.size,
       avg(re.star),
       count(distinct re.id),
       case
           when (count(distinct ro.cafe_id) >= 1)
               then abs(sign(1))
           else 0
           end,
       c.parking,
       c.restaurant,
       c.opened_at,
       c.closed_at
from cafe c
         left join review re on re.cafe_id = c.id
         left join room ro on ro.cafe_id = c.id
         left join fee f on f.cafe_id = c.id
where c.size <= 500000
  and c.parking = false
  and c.restaurant = true
  and c.multi_family = false
  and c.opened_at >= '00:00'
  and c.closed_at <= '23:59'
group by c.id
having avg(re.star) between 0.0 and 5.0
order by avg(re.star);

select c.id,
       c.name,
       c.address,
       c.size,
       avg(re.star),
       count(distinct re.id),
       c.day_off,
       c.multi_family,
       case
           when (count(distinct ro.cafe_id) >= 1)
               then abs(sign(1))
           else 0
           end,
       c.parking,
       c.restaurant,
       c.hyperlink,
       c.opened_at,
       c.closed_at
from cafe c
         left join review re on re.cafe_id = c.id
         left join room ro on ro.cafe_id = c.id
         left join fee f on f.cafe_id = c.id
where c.user_id = 2
group by c.id
order by c.name asc;


# 해당 시간대에
# 해당 방에
# 인원이 다찼는지 (지금 예약하는 사람 포함)


select count(c.id) > 0 as is_exist
from room ro
         left join cafe c on ro.cafe_id = c.id
         left join (SELECT rd.target_id, SUM(rd.count) as reserved_count # 에 포함되는 예약 인원수 as reserved_count
                    from reservation_detail rd
                             join reservation re on rd.reservation_id = re.id
                    where '2024-12-12 10:00:00' between re.started_at and re.finished_at # 예약 시작 시간
                      and '2024-12-12 12:00:00' between re.started_at and re.finished_at # 예약 끝나는 시간
                    group by rd.target_id) res on ro.id = res.target_id

where (res.reserved_count IS NULL OR res.reserved_count < ro.max_count) # 예약 인원이 초과되었는지
  and (c.day_off like '%일%' # 가게가 오픈 상태인지
    and c.opened_at >= '00:00'
    and c.opened_at <= '23:59');

select rd.target_id,
       sum(rd.count)
from reservation r
         left join reservation_detail rd
                   on rd.reservation_id = r.id
where rd.target_id = 1
  and (
    r.started_at >= '2024-12-05 14:00:00'
        and r.finished_at <= '2024-12-05 14:00:00'
    )
  and (
    r.started_at >= '2024-12-05 16:00:00'
        and r.finished_at <= '2024-12-05 16:00:00'
    )
group by rd.target_id

select *
from room ro
         left join reservation_detail rd on ro.id = rd.target_id
         left join reservation re on re.id = rd.reservation_id
where ro.id = 1;

# 모르겠음 초심으로 돌아가자
# 방 아이디가 1인 예약 건수들을 찾는다.
select re.id
from reservation re
         left join reservation_detail rd on re.id = rd.reservation_id
where rd.target_id = 1 and rd.target_type = 'ROOM';

# 1번 방에 예약한 인원을 찾는다.
select sum(rd.count)
from reservation_detail rd
where rd.reservation_id in (select re.id
                            from reservation re
                                     left join reservation_detail rd on re.id = rd.reservation_id
                            where rd.target_id = 1 and rd.target_type = 'ROOM')
  and target_type='FEE';

# 1번 방에 14~16시 사이에 예약한 인원을 찾는다.
select sum(rd.count)
from reservation_detail rd left join reservation re on rd.reservation_id = re.id
where rd.reservation_id in (select re.id
                            from reservation re
                                     left join reservation_detail rd on re.id = rd.reservation_id
                            where rd.target_id = 1 and rd.target_type = 'ROOM')
  and target_type='FEE'
  and ( re.finished_at >= '2024-12-05 14:00:00' and re.started_at <= '2024-12-05 16:00:00');

#  1번카페가 영업중일 때고, 1번방에 14~16시 사이에 예약한 인원을 찾는다.
select sum(rd.count)
from reservation_detail rd
         left join reservation re on rd.reservation_id = re.id
         left join cafe c on c.id = re.cafe_id
where rd.reservation_id in (select re.id
                            from reservation re
                                     left join reservation_detail rd on re.id = rd.reservation_id
                            where rd.target_id = 1 and rd.target_type = 'ROOM')
  and target_type='FEE'
  and ( re.finished_at >= '2024-12-05 14:00:00' and re.started_at <= '2024-12-05 16:00:00')
  and c.closed_at >= '2024-12-05 14:00:00' and c.opened_at <= '2024-12-05 16:00:00'
  and c.day_off not like '%화%';

#  1번카페가 영업중일 때고, 1번방에 14~16시 사이에 예약한 인원과 방의 정원을 찾는다.
select sum(rd.count) as total
from reservation_detail rd
         left join reservation re on rd.reservation_id = re.id
         left join cafe c on c.id = re.cafe_id
where rd.reservation_id in (select re.id
                            from reservation re
                                     left join reservation_detail rd on re.id = rd.reservation_id
                            where rd.target_id = 1 and rd.target_type = 'ROOM')
  and target_type='FEE'
  and ( re.finished_at >= '2024-12-05 14:00:00' and re.started_at <= '2024-12-05 16:00:00')
  and c.closed_at >= '2024-12-05 14:00:00' and c.opened_at <= '2024-12-05 16:00:00'
  and c.day_off not like '%화%';

#  1번카페가 영업중일 때고, 1번방에 14~16시 사이에 예약한 인원과 방의 정원을 찾는다. [새로운 주문 3명]
select sum(rd.count) + 3 <= (select max_count from room where room.id = 1) as isAvailable
from reservation_detail rd
         left join reservation re on rd.reservation_id = re.id
         left join cafe c on c.id = re.cafe_id
where rd.reservation_id in (select re.id
                            from reservation re
                                     left join reservation_detail rd on re.id = rd.reservation_id
                            where rd.target_id = 1 and rd.target_type = 'ROOM')
  and target_type='FEE'
  and ( re.finished_at >= '2024-12-05 14:00:00' and re.started_at <= '2024-12-05 16:00:00')
  and c.closed_at >= '2024-12-05 14:00:00' and c.opened_at <= '2024-12-05 16:00:00'
  and c.day_off not like '%화%';
# 필요한 조건 변수 ..
# 1. 총 인원
# 2. 방 번호
# 3. 예매 시작 시간
# 4. 예매 마감 시간