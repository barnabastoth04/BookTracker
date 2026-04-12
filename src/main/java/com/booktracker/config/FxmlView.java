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
    SEARCH_BY_NAME {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/search-by-name.fxml";
        }
    },
    SEARCH_BY_COPY {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/search-by-copy.fxml";
        }
    },
    BOOK {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/book.fxml";
        }
    },
    READINGS {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/readings.fxml";
        }
    },
    RATINGS {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/ratings.fxml";
        }
    },
    ACHIEVEMENTS {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/achievements.fxml";
        }
    },
    STATISTICS {
        @Override
        public String getFxmlPath() {
            return "/com/booktracker/view/statistics.fxml";
        }
    };

    public abstract String getFxmlPath();
}
