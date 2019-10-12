package generators;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.color.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.IRenderer;
import cvObjects.LifeEvent;
import cvObjects.Person;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.time.format.DateTimeFormatter;

public class CvGenerator {
    private static final String FONT = "/home/marcin/.fonts/NotoSans-Light.ttf";
    private static PdfFont documentsFont;
    private Person person;
    private int fontSize;

    public CvGenerator(Person person, int fontSize){
        this.person = person;
        this.fontSize = fontSize;
        try {
            documentsFont = PdfFontFactory.createFont(FONT, "Cp1250", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public float generateCv(String pdfDestination){
        Document document = openPDF(pdfDestination);
        document.setMargins(0,0,0,0);
        Table table = addMainContainer();
        document.add(table);
        IRenderer paragraphRenderer = table.createRendererSubTree();
        LayoutResult result = paragraphRenderer.setParent(document.getRenderer()).
                layout(new LayoutContext(new LayoutArea(1, new Rectangle(PageSize.A4.getWidth(),  10000))));

        System.out.println("height " + result.getOccupiedArea().getBBox().getHeight());
        document.close();
        return result.getOccupiedArea().getBBox().getHeight();
    }

    private Document openPDF(String destination){
        PdfWriter writer;
        try {
            writer = new PdfWriter(destination);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
        PdfDocument pdf = new PdfDocument(writer);
        pdf.setDefaultPageSize(PageSize.A4);
        return new Document(pdf);
    }

    private Table addMainContainer(){
        float [] widths = {1,2};
        Table mainContainer = new Table(UnitValue.createPercentArray(widths));
        mainContainer.setWidthPercent(100);
        mainContainer.setMinHeight(PageSize.A4.getHeight());
        mainContainer.setFont(documentsFont);
        mainContainer.addCell(addPrivateDataContainer());
        mainContainer.addCell(addCrucialInfoContainer());
        return mainContainer;
    }

    private Cell addPrivateDataContainer(){
        Cell data = new Cell().setBorder(Border.NO_BORDER);
        Color myColor = new DeviceRgb(53, 53, 53);
        data.setBackgroundColor(myColor);

        Paragraph photo = new Paragraph().add(addPersonPhoto()/*.setRotationAngle(90 * Math.PI / 180)*/.scaleToFit(175,260)).setTextAlignment(TextAlignment.CENTER);
        photo.setMarginBottom(25);

        Paragraph birth = titlePersonalData("Data Urodzenia:");

        Paragraph birthday = personalData(person.getDateOfBirth().toString());

        Paragraph address = titlePersonalData("Adres:");
        address.setFixedLeading(12);

        Paragraph livingPlace = personalData(person.getAddress().toString());
        livingPlace.setFixedLeading(12);

        Paragraph phoneNumberTitle = titlePersonalData("Telefon:");

        Paragraph phoneNumber = personalData(String.valueOf(person.getPhoneNumber()));

        Paragraph emailTitle = titlePersonalData("E-mail:");

        Paragraph email = personalData(person.getEmailAddress());

        data.add(nameAndSurname());
        data.add(photo);
        data.add(birth);
        data.add(birthday);
        data.add(address);
        data.add(livingPlace);
        data.add(phoneNumberTitle);
        data.add(phoneNumber);
        data.add(emailTitle);
        data.add(email);

        return data;
    }

    private Cell nameAndSurname(){
        Cell data = new Cell();
        Paragraph name = new Paragraph(person.getName());
        name.setFontSize(18);
        name.setFontColor(Color.WHITE);
        name.setMarginLeft(30);
        name.setMarginTop(50);
        Paragraph surname = new Paragraph(person.getSurname()).setMarginBottom(10);
        surname.setFontSize(20);
        surname.setBold();
        surname.setFontColor(Color.WHITE);
        surname.setMarginLeft(30);
        surname.setMarginBottom(30);
        surname.setFixedLeading(20);
        data.add(name);
        data.add(surname);
        return data;
    }

    private Paragraph titlePersonalData(String titleText){
        Paragraph title = new Paragraph(titleText);
        title.setFontSize(12);
        title.setFontColor(Color.WHITE);
        title.setMarginLeft(30);
        title.setFixedLeading(5);
        title.setCharacterSpacing((float) 1.6);
        title.setBold();
        return title;
    }

    private Paragraph personalData(String dataText){
        Paragraph data = new Paragraph(dataText);
        data.setFontSize(12);
        data.setFontColor(Color.WHITE);
        data.setMarginLeft(30);
        data.setMarginBottom(20);
        return data;
    }

    private Image addPersonPhoto(){
        ImageData data = null;
        try {
            data = ImageDataFactory.create(person.getImageFile());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return  new Image(data);
    }

    private Cell addCrucialInfoContainer(){
        Cell data = new Cell().setBorder(Border.NO_BORDER);
        data.setPadding(10);

        data.add(crucialDataParagraph(person.getShortInfo()));

        data.add(crucialDataTitle("• DOŚWIADCZENIE"));
            for (LifeEvent e : person.getExperience()) {
                data.add(lifeEventTitleParagraph(e));
                data.add(lifeEventDescriptionParagraph(e));
            }

        data.add(crucialDataTitle("• WYKSZTAŁCENIE"));
        for(LifeEvent e: person.getEducation()){
            data.add(lifeEventTitleParagraph(e));
            data.add(lifeEventDescriptionParagraph(e));
        }

        data.add(crucialDataTitle("• KURSY I SZKOLENIA"));
            for (LifeEvent e : person.getCourses()) {
                data.add(lifeEventTitleParagraph(e));
                data.add(lifeEventDescriptionParagraph(e));
            }

        data.add(crucialDataTitle("• UMIEJĘTNOŚCI").setMarginBottom(fontSize));
        list(person.getSkills(),data);

        data.add(crucialDataTitle("• ZAINTERESOWANIA").setMarginBottom(fontSize));
        list(person.getInterest(),data);

        Paragraph p = new Paragraph("Wyrażam zgodę na przetwarzanie moich danych osobowych dla potrzeb niezbędnych do realizacji" +
                " procesu tej oraz przyszłych rekrutacji (zgodnie z ustawą z dnia 10 maja 2018 roku o ochronie danych osobowych" +
                " (Dz. Ustaw z 2018, poz. 1000) oraz zgodnie z Rozporządzeniem Parlamentu Europejskiego i Rady (UE) 2016/679 z " +
                "dnia 27 kwietnia 2016 r. w sprawie ochrony osób fizycznych w związku z przetwarzaniem danych osobowych " +
                "i w sprawie swobodnego przepływu takich danych oraz uchylenia dyrektywy 95/46/WE (RODO)).");
        if(fontSize > 7) {
            p.setFontSize(7);
            p.setFixedLeading(7);
        }
        else{
            if(fontSize > 2) {
                p.setFontSize(fontSize - 2);
                p.setFixedLeading(fontSize - 2);
            }
            else{
                p.setFontSize(1);
                p.setFixedLeading(1);
            }
        }
        p.setMarginTop(30);
        data.add(p);

        return data;
    }

    private void list(java.util.List<String> elements, Cell data){
        for(String e:elements){
            Paragraph p = new Paragraph("- " + e);
            p.setFixedLeading(fontSize);
            p.setFontSize(fontSize);
            data.add(p);
        }
    }

    private Paragraph crucialDataParagraph(String text){
        Paragraph paragraph = new Paragraph();
        paragraph.setFontSize(fontSize);
        paragraph.setFixedLeading(fontSize);
        paragraph.setTextAlignment(TextAlignment.JUSTIFIED);
        paragraph.add(text);
        return paragraph;
    }

    private Paragraph crucialDataTitle(String titleText){
        Paragraph title = new Paragraph();
        title.setFontSize(fontSize + 2);
        title.setBold();
        title.setTextAlignment(TextAlignment.LEFT);
        title.setWidthPercent(100);
        title.setFixedLeading(fontSize + 5);
        title.setCharacterSpacing((float)1.5);
        SolidBorder border = new SolidBorder((float) 0.75);
        border.setColor(Color.GRAY);
        title.add(titleText);
        title.setBorderBottom(border);

        title.setMarginTop(fontSize);
        return title;
    }

    private Paragraph lifeEventTitleParagraph(LifeEvent event){
        Paragraph lifeEventTitleParagraph = new Paragraph();
        lifeEventTitleParagraph.setFontSize(fontSize);
        lifeEventTitleParagraph.setBold();
        lifeEventTitleParagraph.setFixedLeading(fontSize);
        lifeEventTitleParagraph.setTextAlignment(TextAlignment.LEFT);
        lifeEventTitleParagraph.setMarginTop(fontSize);
        if(event.getEnd() != null && event.getBegin() != null) {
            lifeEventTitleParagraph.add(event.getTitle() + "   ( " + event.getBegin().
                    format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + "  -  " + event.getEnd().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")) + " )");
        }
        else if(event.getBegin() != null){
            lifeEventTitleParagraph.add(event.getTitle() + "   od   " + event.getBegin().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));

        }
        else{
            lifeEventTitleParagraph.add(event.getTitle());
        }

        return lifeEventTitleParagraph;
    }

    private Paragraph lifeEventDescriptionParagraph(LifeEvent event){
        Paragraph lifeEventDescriptionParagraph = new Paragraph();
        if(event.getDescription().equals("")){
            lifeEventDescriptionParagraph.setHeight(0);
        }
        else {
            lifeEventDescriptionParagraph.setFontSize(fontSize);
            lifeEventDescriptionParagraph.setFixedLeading(fontSize);
            lifeEventDescriptionParagraph.setTextAlignment(TextAlignment.JUSTIFIED);
            lifeEventDescriptionParagraph.add(event.getDescription());
        }
        return lifeEventDescriptionParagraph;
    }
}
