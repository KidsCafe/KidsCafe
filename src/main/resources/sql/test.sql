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