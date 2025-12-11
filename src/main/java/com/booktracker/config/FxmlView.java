package com.booktracker.config;

public enum FxmlView {
    START {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/booktracker-application.fxml";
        }
    },
    SIGN_UP {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/sign-up.fxml";
        }
    },
    SIGN_IN {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/sign-in.fxml";
        }
    },
    USER {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/user.fxml";
        }
    },
    LIBRARY {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/library.fxml";
        }
    },
    WISHLIST {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/wishlist.fxml";
        }
    },
    SEARCH {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/search.fxml";
        }
    },
    BOOK {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/book.fxml";
        }
    };

    public abstract String getFxmlPath();
}
