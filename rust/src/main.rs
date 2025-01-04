#![allow(rustdoc::missing_crate_level_docs)]

use chrono::Datelike;

fn main() -> eframe::Result {
    let options = eframe::NativeOptions {
        viewport: egui::ViewportBuilder::default().with_inner_size([320., 120.]),
        ..Default::default()
    };
    eframe::run_native("dnc", options, Box::new(|_| Ok(Box::<MyApp>::default())))
}

struct MyApp {
    day: String,
    month: String,
    happy_year: String,
}

impl MyApp {
    fn check_heppy_year(&mut self) {
        if self.day.parse::<u32>().is_ok()
            && self.month.parse::<u32>().is_ok()
            && chrono::NaiveDate::parse_from_str(
                format!("{}.{}.2000", self.day, self.month).as_str(),
                "%d.%m.%Y",
            )
            .is_ok()
        {
            let day_rem = self
                .day
                .chars()
                .fold(0, |sum, c| sum + c.to_string().parse::<u32>().unwrap())
                % 10;
            let month_rem = self
                .month
                .chars()
                .fold(0, |sum, c| sum + c.to_string().parse::<u32>().unwrap())
                % 10;
            let now = chrono::offset::Local::now().year();
            for year in now..9999 {
                let year_rem = year
                    .to_string()
                    .chars()
                    .fold(0, |sum, c| sum + c.to_string().parse::<u32>().unwrap())
                    % 10;
                if year_rem == day_rem || month_rem == day_rem {
                    self.happy_year = year.to_string();
                    return;
                }
            }
        }
    }
}

impl Default for MyApp {
    fn default() -> Self {
        Self {
            day: "1".to_owned(),
            month: "1".to_owned(),
            happy_year: "".to_owned(),
        }
    }
}

impl eframe::App for MyApp {
    fn update(&mut self, ctx: &egui::Context, _frame: &mut eframe::Frame) {
        egui::CentralPanel::default().show(ctx, |ui| {
            ui.label(format!(
                "{}\n{}\n{}",
                "Введите день и месяц своего рождения",
                "После нажатия кнопки \"Ок\" выведется ваш",
                "ближайший счастливый год"
            ));
            ui.horizontal(|ui| {
                ui.allocate_ui_with_layout(
                    egui::Vec2::new(50.0, 20.0),
                    egui::Layout::left_to_right(egui::Align::Center),
                    |ui| {
                        let day_label = ui.label("День:");
                        ui.text_edit_singleline(&mut self.day)
                            .labelled_by(day_label.id);
                    },
                );

                ui.allocate_ui_with_layout(
                    egui::Vec2::new(50.0, 20.0),
                    egui::Layout::left_to_right(egui::Align::Center),
                    |ui| {
                        let month_label = ui.label("Месяц:");
                        ui.text_edit_singleline(&mut self.month)
                            .labelled_by(month_label.id);
                    },
                );
                if ui.button("Ok").clicked() {
                    self.check_heppy_year()
                }
            });
            ui.label(format!("Ваш ближайший год: {}", self.happy_year));
        });
    }
}
