```mermaid
erDiagram
    app_user {
        bigint id PK
        varchar username UK
        varchar password
        varchar name
    }

    app_user_roles {
        bigint app_user_id FK
        integer roles
    }

    hotels {
        bigint id PK
        varchar name
        varchar city
        text_array photos
        text_array amenties
        timestamp createdAt
        timestamp updatedAt
        boolean active
        varchar phoneNumber
        varchar address
        varchar email
        varchar website
        bigint owner_id FK
    }

    room {
        bigint id PK
        bigint hotel_id FK
        varchar Type
        decimal baseprice
        text_array amenties
        timestamp createdAt
        timestamp updatedAt
        integer totalcount
        integer capacity
    }

    inventory {
        bigint id PK
        bigint hotel_id FK
        bigint room_id FK
        date date
        integer bookedCount
        integer reservedCount
        integer totalCount
        decimal surgefactor
        decimal price
        varchar city
        boolean closed
        timestamp createdAt
        timestamp updatedAt
    }

    Guest {
        bigint id PK
        bigint user_id FK
        varchar gender
    }

    bookings {
        bigint id PK
        bigint hotel_id FK
        decimal amount
        bigint room_id FK
        bigint user_id FK
        integer roomsCount
        date checkInDate
        date checkOutDate
        timestamp createdAt
        timestamp updatedAt
        varchar bookingStatus
        varchar createdBy
        timestamp createdDate
        varchar lastModifiedBy
        timestamp lastModifiedDate
    }

    booking_guests {
        bigint booking_id PK_FK
        bigint guest_id PK_FK
    }

    Payment {
        bigint id PK
        varchar transactionId UK
        varchar status
        decimal amount
        timestamp createdAt
        timestamp updatedAt
        bigint Booking_id FK
    }

    app_user ||--o{ hotels : "owns"
    app_user ||--o{ Guest : "has_profile"
    app_user ||--o{ bookings : "makes"
    app_user ||--|{ app_user_roles : "has_roles"
    
    hotels ||--o{ room : "contains"
    hotels ||--o{ inventory : "tracks_availability"
    hotels ||--o{ bookings : "receives_bookings"
    
    room ||--o{ inventory : "has_daily_inventory"
    room ||--o{ bookings : "booked_in"
    
    bookings ||--|| Payment : "has_payment"
    bookings ||--|{ booking_guests : "includes"
    Guest ||--|{ booking_guests : "included_in"